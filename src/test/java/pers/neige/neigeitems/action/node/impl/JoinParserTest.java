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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("JoinParser")
public class JoinParserTest {
    private ActionContext context;
    private JoinParser parser;

    @BeforeEach
    void setUp() {
        val manager = mock(BaseActionManager.class);
        context = mock(ActionContext.class);
        val plugin = mock(Plugin.class);
        when(plugin.getLogger()).thenReturn(Logger.getLogger("JoinParserTest"));
        when(manager.getPlugin()).thenReturn(plugin);
        when(manager.parseNullableNode(nullable(String.class), any(ActionContext.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        when(manager.parseNode(nullable(String.class), any(ActionContext.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        when(context.getCaster()).thenReturn(null);
        parser = new JoinParser(manager);
    }

    @Test
    void shouldExposeId() {
        assertEquals("join", parser.getId());
    }

    @Test
    void shouldJoinValuesWithDefaults() {
        assertEquals("a, b, c", parse(
            "values", Arrays.asList("a", "b", "c")
        ));
    }

    @Test
    void shouldSupportPrefixPostfixLimitAndTruncated() {
        assertEquals("[a|b|...]", parse(
            "values", Arrays.asList("a", "b", "c"),
            "separator", "|",
            "prefix", "[",
            "postfix", "]",
            "limit", "2",
            "truncated", "..."
        ));
    }

    @Test
    void shouldAcceptLegacyListKeyAndShuffleWithoutLosingElements() {
        assertEquals("a, b", parse("list", Arrays.asList("a", "b")));
        val shuffled = parse(
            "values", Arrays.asList("a", "b", "c"),
            "shuffled", "true"
        );
        assertEquals(new HashSet<>(Arrays.asList("a", "b", "c")), new HashSet<>(Arrays.asList(shuffled.split(", "))));
    }

    @Test
    void shouldReturnPrefixPostfixForEmptyList() {
        // 旧实现：空 list 仍返回 prefix+postfix，而不是 null
        assertEquals("", parse("values", Collections.emptyList()));
        assertEquals("[]", parse(
            "values", Collections.emptyList(),
            "prefix", "[",
            "postfix", "]"
        ));
    }

    @Test
    void shouldSoftFailInvalidLimitAndShuffled() {
        // 非法 limit → 无限制
        assertEquals("a, b", parse("values", Arrays.asList("a", "b"), "limit", "x"));
        // 非法 shuffled → false（不失败）
        assertEquals("a, b", parse("values", Arrays.asList("a", "b"), "shuffled", "yes"));
        // 缺 list → null
        assertNull(parse());
    }

    @Test
    void shouldMatchSectionForBasicJoin() {
        // 对比用例统一用 list（旧 section 仅认 list）
        assertNodeEqualsSection(config(
            "list", Arrays.asList("a", "b", "c")
        ));
    }

    @Test
    void shouldMatchSectionForPrefixLimitAndTruncated() {
        assertNodeEqualsSection(config(
            "list", Arrays.asList("a", "b", "c"),
            "separator", "|",
            "prefix", "[",
            "postfix", "]",
            "limit", "2",
            "truncated", "..."
        ));
    }

    @Test
    void shouldMatchSectionForEmptyListAndSoftFail() {
        assertNodeEqualsSection(config(
            "list", Collections.emptyList()
        ));
        assertNodeEqualsSection(config(
            "list", Collections.emptyList(),
            "prefix", "[",
            "postfix", "]"
        ));
        assertNodeEqualsSection(config(
            "list", Arrays.asList("a", "b"),
            "limit", "x"
        ));
        assertNodeEqualsSection(config(
            "list", Arrays.asList("a", "b"),
            "shuffled", "yes"
        ));
        // 注意：section 的 getStringList 缺键返回空列表，node 缺键返回 null，不在此对比
    }

    @Test
    void shouldMatchSectionForTransformWhenNashornAvailable() {
        assumeTrue(isNashornAvailable(), "nashorn unavailable");
        assertNodeEqualsSection(config(
            "list", Arrays.asList("a", "b", "c"),
            "transform", "return this.it + this.index"
        ));
    }

    private void assertNodeEqualsSection(Map<String, Object> map) {
        val nodeResult = parser.parse(context, ConfigReader.parse(map));
        val sectionResult = pers.neige.neigeitems.section.impl.JoinParser.INSTANCE.onRequest(
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
