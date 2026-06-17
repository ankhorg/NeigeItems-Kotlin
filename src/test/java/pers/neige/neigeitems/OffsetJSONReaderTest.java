package pers.neige.neigeitems;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.OffsetJSONReader;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class OffsetJSONReaderTest {
    // ── Basic non-greedy parsing ──

    @Test
    public void testSimpleNoTrailing() {
        val r = OffsetJSONReader.parseObject(
                "{\"test\":\"111\"}"
        );
        assertEquals("111", r.object.get("test"));
        assertTrue(r.endOffset > 0);
        // endOffset should be right after '}'
        byte[] bytes = "{\"test\":\"111\"}".getBytes(StandardCharsets.UTF_8);
        assertEquals(bytes.length, r.endOffset);
    }

    @Test
    public void testTrailingRandomText() {
        String json = "{\"test\":\"111\"} fcwefsefrwe";
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

        val r = OffsetJSONReader.parseObject(bytes);

        assertEquals("111", r.object.get("test"));
        // The trailing text should start at endOffset
        String trailing = new String(bytes, r.endOffset, bytes.length - r.endOffset, StandardCharsets.UTF_8);
        assertTrue(trailing.startsWith(" "), "trailing should start with space, got: '" + trailing + "'");
    }

    @Test
    public void testTrailingComma() {
        val r = OffsetJSONReader.parseObject(
                "{\"a\":1}    , extra stuff"
        );
        assertEquals(1, r.object.get("a"));
        byte[] bytes = "{\"a\":1}    , extra stuff".getBytes(StandardCharsets.UTF_8);
        String trailing = new String(bytes, r.endOffset, bytes.length - r.endOffset, StandardCharsets.UTF_8);
        assertTrue(trailing.startsWith(" "), "trailing should start with spaces: '" + trailing + "'");
    }

    @Test
    public void testEmpty() {
        val r = OffsetJSONReader.parseObject("{}");
        assertTrue(r.object.isEmpty());
        byte[] bytes = "{}".getBytes(StandardCharsets.UTF_8);
        assertEquals(bytes.length, r.endOffset);
    }

    @Test
    public void testNestedObject() {
        String json = "{\"outer\":{\"inner\":\"val\"}} garbage";
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

        val r = OffsetJSONReader.parseObject(bytes);

        assertInstanceOf(Map.class, r.object.get("outer"));
        Map<?, ?> inner = (Map<?, ?>) r.object.get("outer");
        assertEquals("val", inner.get("inner"));

        // Offset should be after the outer '}', not the inner one
        String trailing = new String(bytes, r.endOffset, bytes.length - r.endOffset, StandardCharsets.UTF_8);
        assertTrue(trailing.startsWith(" "), "trailing should start with space, got: '" + trailing + "'");
    }

    @Test
    public void testNestedArray() {
        String json = "{\"arr\":[1,2,3]} trailing";
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

        val r = OffsetJSONReader.parseObject(bytes);

        assertInstanceOf(List.class, r.object.get("arr"));
        String trailing = new String(bytes, r.endOffset, bytes.length - r.endOffset, StandardCharsets.UTF_8);
        assertTrue(trailing.startsWith(" "), "trailing should start with space, got: '" + trailing + "'");
    }

    // ── Features ──

    @Test
    public void testAllowUnQuotedFieldNames() {
        val r = OffsetJSONReader.parseObject(
                "{test:\"111\"}",
                JSONReader.Feature.AllowUnQuotedFieldNames
        );
        assertEquals("111", r.object.get("test"));
    }

    @Test
    public void testUnquotedNumericKey() {
        val r = OffsetJSONReader.parseObject(
                "{1:\"test\",2:\"val\"} trailing",
                JSONReader.Feature.AllowUnQuotedFieldNames
        );
        assertEquals("test", r.object.get("1"));
        assertEquals("val", r.object.get("2"));
    }

    @Test
    public void testAllThreeFeatures() {
        val r = OffsetJSONReader.parseObject(
                "{code:1,msg:\"Hello world\"} trailing garbage",
                JSONReader.Feature.AllowUnQuotedFieldNames,
                JSONReader.Feature.IgnoreCheckClose,
                JSONReader.Feature.NonStringKeyAsString
        );
        assertEquals(1, r.object.get("code"));
        assertEquals("Hello world", r.object.get("msg"));
        assertTrue(r.endOffset > 0);
    }

    @Test
    public void testStringInput() {
        val r = OffsetJSONReader.parseObject(
                "{\"key\":\"value\"} extra",
                JSONReader.Feature.AllowUnQuotedFieldNames,
                JSONReader.Feature.IgnoreCheckClose,
                JSONReader.Feature.NonStringKeyAsString
        );
        assertEquals("value", r.object.get("key"));
    }

    // ── Edge cases ──

    @Test
    public void testMultipleValues() {
        // JSON with number and boolean
        val r = OffsetJSONReader.parseObject(
                "{\"num\":42,\"flag\":true,\"str\":\"hello\"}  extra"
        );
        assertEquals(42, r.object.get("num"));
        assertEquals(true, r.object.get("flag"));
        assertEquals("hello", r.object.get("str"));
    }

    @Test
    public void testNullValue() {
        val r = OffsetJSONReader.parseObject(
                "{\"val\":null} trailing"
        );
        assertNull(r.object.get("val"));
    }

    @Test
    public void testSingleCharKeyValue() {
        val r = OffsetJSONReader.parseObject(
                "{\"a\":\"b\"} trailing"
        );
        assertEquals("b", r.object.get("a"));
    }

    @Test
    public void testOffsetConsistency() {
        // Parse the same JSON multiple times and verify offset is consistent
        String json = "{\"x\":1} trailing";
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

        for (int i = 0; i < 5; i++) {
            val r = OffsetJSONReader.parseObject(bytes);
            assertEquals(1, r.object.get("x"));
            assertEquals(7, r.endOffset); // bytes: { " x " : 1 } = 7 bytes
        }
    }
}
