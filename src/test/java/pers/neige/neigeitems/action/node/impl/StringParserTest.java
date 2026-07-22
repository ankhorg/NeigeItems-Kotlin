package pers.neige.neigeitems.action.node.impl;

import lombok.val;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("StringParser")
public class StringParserTest {
    private ActionContext context;
    private StringParser parser;

    @BeforeEach
    void setUp() {
        val manager = mock(BaseActionManager.class);
        context = mock(ActionContext.class);
        val plugin = mock(Plugin.class);
        when(plugin.getLogger()).thenReturn(Logger.getLogger("StringParserTest"));
        when(manager.getPlugin()).thenReturn(plugin);
        when(manager.parseNullableNode(nullable(String.class), any(ActionContext.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        parser = new StringParser(manager);
    }

    @Test
    void shouldExposeId() {
        assertEquals("string", parser.getId());
    }

    @Test
    void shouldTransformCaseTrimAndLength() {
        assertEquals("abc", parse("mode", "lower", "value", "AbC"));
        assertEquals("ABC", parse("mode", "upper", "value", "AbC"));
        assertEquals("x", parse("mode", "trim", "value", "  x  "));
        assertEquals("3", parse("mode", "length", "value", "abc"));
    }

    @Test
    void shouldSupportContainsStartsAndEnds() {
        assertEquals("true", parse("mode", "contains", "value", "abc", "arg", "b"));
        assertEquals("false", parse("mode", "contains", "value", "abc", "arg", "z"));
        assertEquals("true", parse("mode", "starts-with", "value", "abc", "arg", "ab"));
        assertEquals("true", parse("mode", "ends-with", "value", "abc", "arg", "bc"));
    }

    @Test
    void shouldSubstringAndReplaceLiteral() {
        assertEquals("ell", parse("mode", "substring", "value", "hello", "start", "1", "end", "4"));
        assertEquals("ello", parse("mode", "substring", "value", "hello", "start", "1"));
        assertEquals("fallback", parse("mode", "substring", "value", "hello", "start", "9", "default", "fallback"));
        assertEquals("xby", parse("mode", "replace-literal", "value", "xay", "target", "a", "replacement", "b"));
    }

    @Test
    void shouldParseInlineAndRejectInvalidInput() {
        assertEquals("abc", parser.parse(context, "lower_AbC"));
        assertEquals("true", parser.parse(context, "contains_a\\_b_a\\_"));
        assertEquals("xby", parser.parse(context, "replace-literal_xay_a_b"));
        assertNull(parser.parse(context, "unknown_a"));
        assertNull(parser.parse(context, "lower"));
        assertNull(parse("mode", "substring", "value", "hello", "start", "x"));
    }

    private String parse(Object... values) {
        return parser.parse(context, ConfigReader.parse(config(values)));
    }

    private static Map<String, Object> config(Object... values) {
        val result = new HashMap<String, Object>();
        for (int index = 0; index < values.length; index += 2) {
            if (values[index + 1] != null) result.put(values[index].toString(), values[index + 1]);
        }
        return result;
    }
}
