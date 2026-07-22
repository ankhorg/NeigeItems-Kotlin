package pers.neige.neigeitems.action.node.impl;

import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.result.Results;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("WhenParser")
public class WhenParserTest {
    private BaseActionManager manager;
    private ActionContext context;
    private WhenParser parser;
    private Map<String, Object> cache;
    private MockedStatic<Bukkit> mockedBukkit;

    @BeforeEach
    void setUp() {
        mockedBukkit = Mockito.mockStatic(Bukkit.class);
        mockedBukkit.when(Bukkit::isPrimaryThread).thenReturn(false);

        manager = mock(BaseActionManager.class);
        context = mock(ActionContext.class);
        cache = new HashMap<>();
        val plugin = mock(Plugin.class);
        when(plugin.getLogger()).thenReturn(Logger.getLogger("WhenParserTest"));
        when(manager.getPlugin()).thenReturn(plugin);
        when(manager.parseNullableNode(nullable(String.class), any(ActionContext.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        when(manager.parseNode(nullable(String.class), any(ActionContext.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        when(context.getSectionCache()).thenReturn(cache);
        when(context.getPlayer()).thenReturn(null);
        // 条件求值：根据 condition 字符串返回 SUCCESS/STOP（仅 node 单侧 mock 测）
        when(manager.parseCondition(nullable(String.class), any(ActionContext.class)))
            .thenAnswer(invocation -> {
                val condition = invocation.getArgument(0, String.class);
                if (condition == null || condition.isEmpty() || "true".equals(condition)) {
                    return Results.SUCCESS;
                }
                if ("false".equals(condition)) {
                    return Results.STOP;
                }
                return Results.SUCCESS;
            });
        parser = new WhenParser(manager);
    }

    @AfterEach
    void tearDown() {
        if (mockedBukkit != null) {
            mockedBukkit.close();
        }
    }

    @Test
    void shouldExposeId() {
        assertEquals("when", parser.getId());
    }

    @Test
    void shouldReturnFirstMatchingBranch() {
        val first = new HashMap<String, Object>();
        first.put("condition", "false");
        first.put("result", "no");
        val second = new HashMap<String, Object>();
        second.put("condition", "true");
        second.put("result", "yes");

        assertEquals("yes", parse(
            "value", "demo",
            "conditions", Arrays.asList(first, second, "fallback")
        ));
        assertFalse(cache.containsKey("value"));
    }

    @Test
    void shouldUseDefaultBranchAndCleanCache() {
        val branch = new HashMap<String, Object>();
        branch.put("condition", "false");
        branch.put("result", "no");

        assertEquals("fallback", parse(
            "value", "demo",
            "conditions", Arrays.asList(branch, "fallback")
        ));
        assertFalse(cache.containsKey("value"));
    }

    @Test
    void shouldReturnNullWhenNothingMatches() {
        val branch = new HashMap<String, Object>();
        branch.put("condition", "false");
        branch.put("result", "no");

        assertNull(parse("conditions", Arrays.asList(branch)));
        assertFalse(cache.containsKey("value"));
    }

    @Test
    void shouldTreatMissingResultAsNullString() {
        val branch = new HashMap<String, Object>();
        branch.put("condition", "true");
        // 无 result → 旧实现 String.valueOf(null) == "null"
        assertEquals("null", parse("conditions", Arrays.asList(branch)));
    }

    @Test
    void shouldMatchSectionDefaultBranch() {
        // 默认分支不触发 parseCondition，可与旧 section 真双跑
        assertNodeEqualsSection(config(
            "conditions", Collections.singletonList("hello")
        ));
        assertNodeEqualsSection(config(
            "value", "demo",
            "conditions", Collections.singletonList("fallback")
        ));
    }

    private void assertNodeEqualsSection(Map<String, Object> map) {
        val nodeResult = parser.parse(context, ConfigReader.parse(map));
        val sectionResult = pers.neige.neigeitems.section.impl.WhenParser.INSTANCE.onRequest(
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
