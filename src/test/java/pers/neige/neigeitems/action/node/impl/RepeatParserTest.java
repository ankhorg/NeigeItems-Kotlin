package pers.neige.neigeitems.action.node.impl;

import lombok.val;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.script.CompiledScript;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("RepeatParser")
public class RepeatParserTest {
    private ActionContext context;
    private RepeatParser parser;

    @BeforeEach
    void setUp() {
        val manager = mock(BaseActionManager.class);
        context = mock(ActionContext.class);
        val plugin = mock(Plugin.class);
        when(plugin.getLogger()).thenReturn(Logger.getLogger("RepeatParserTest"));
        when(manager.getPlugin()).thenReturn(plugin);
        when(manager.parseNullableNode(nullable(String.class), any(ActionContext.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        when(manager.parseNode(nullable(String.class), any(ActionContext.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        when(context.getCaster()).thenReturn(null);
        parser = new RepeatParser(manager);
    }

    @Test
    void shouldExposeId() {
        assertEquals("repeat", parser.getId());
    }

    @Test
    void shouldRepeatContent() {
        assertEquals("★★★", parse("content", "★", "repeat", "3"));
        assertEquals("a,a,a", parse("content", "a", "repeat", "3", "separator", ","));
        assertEquals("[aa]", parse("content", "a", "repeat", "2", "prefix", "[", "postfix", "]"));
        assertEquals("[]", parse("content", "a", "repeat", "0", "prefix", "[", "postfix", "]"));
    }

    @Test
    void shouldParseInlineAndDefaultInvalidRepeat() {
        assertEquals("xx", parser.parse(context, "x_2"));
        assertEquals("x-x", parser.parse(context, "x_2_-"));
        // 旧实现：非法次数 → 默认 1
        assertEquals("a", parse("content", "a", "repeat", "x"));
        // 缺省次数 → 默认 1
        assertEquals("a", parse("content", "a"));
    }

    @Test
    void shouldMatchSectionForBasicRepeat() {
        assertNodeEqualsSection(config(
            "content", "★",
            "repeat", "3"
        ));
        assertNodeEqualsSection(config(
            "content", "a",
            "repeat", "3",
            "separator", ","
        ));
        assertNodeEqualsSection(config(
            "content", "a",
            "repeat", "2",
            "prefix", "[",
            "postfix", "]"
        ));
        assertNodeEqualsSection(config(
            "content", "a",
            "repeat", "0",
            "prefix", "[",
            "postfix", "]"
        ));
    }

    @Test
    void shouldMatchSectionForDefaultAndInvalidRepeat() {
        assertNodeEqualsSection(config(
            "content", "a",
            "repeat", "x"
        ));
        assertNodeEqualsSection(config(
            "content", "a"
        ));
    }

    @Test
    void shouldMatchSectionForTransformWhenNashornAvailable() {
        assumeTrue(isNashornAvailable(), "nashorn unavailable");
        assertNodeEqualsSection(config(
            "content", "a",
            "repeat", "3",
            "transform", "return this.it + this.index"
        ));
    }

    private void assertNodeEqualsSection(Map<String, Object> map) {
        val nodeResult = parser.parse(context, ConfigReader.parse(map));
        // section RepeatParser 返回非 null String
        val sectionResult = pers.neige.neigeitems.section.impl.RepeatParser.INSTANCE.onRequest(
            sectionOf(map), new HashMap<>(), null, null
        );
        assertEquals(sectionResult, nodeResult);
    }

    private static ConfigurationSection sectionOf(Map<String, Object> map) {
        // 不用 YamlConfiguration：与项目 snakeyaml 2.x 冲突（Representer 无无参构造）
        val section = new MemoryConfiguration();
        for (val entry : map.entrySet()) {
            section.set(entry.getKey(), entry.getValue());
        }
        return section;
    }

    private static boolean isNashornAvailable() {
        try {
            new CompiledScript("function main() { return 1 }");
            return true;
        } catch (Throwable ignored) {
            return false;
        }
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
