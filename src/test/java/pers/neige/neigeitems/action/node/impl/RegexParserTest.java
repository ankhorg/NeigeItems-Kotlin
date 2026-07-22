package pers.neige.neigeitems.action.node.impl;

import lombok.val;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("RegexParser")
public class RegexParserTest {
    private BaseActionManager manager;
    private ActionContext context;
    private RegexParser parser;

    @BeforeEach
    void setUp() {
        manager = mock(BaseActionManager.class);
        context = mock(ActionContext.class);
        val plugin = mock(Plugin.class);
        when(plugin.getLogger()).thenReturn(Logger.getLogger("RegexParserTest"));
        when(manager.getPlugin()).thenReturn(plugin);
        when(manager.parseNullableNode(nullable(String.class), any(ActionContext.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        parser = new RegexParser(manager);
    }

    @Test
    void shouldExposeId() {
        assertEquals("regex", parser.getId());
    }

    @Test
    void shouldDistinguishMatchesAndFind() {
        assertEquals("false", parse("matches", "abc123", "\\d+"));
        assertEquals("true", parse("find", "abc123", "\\d+"));
        assertEquals("false", parse("find", "abc", "\\d+"));
    }

    @Test
    void shouldCountMatchesIncludingEmptyMatches() {
        assertEquals("3", parse("count", "a1b22c333", "\\d+"));
        assertEquals("0", parse("count", "abc", "\\d+"));
        assertEquals("3", parse("count", "ab", ""));
    }

    @Test
    void shouldReadNumberedAndNamedGroups() {
        assertEquals("item-12", parseGroup("item-12", "item-(\\d+)", null, null));
        assertEquals("12", parseGroup("item-12", "item-(\\d+)", "1", null));
        assertEquals("12", parseGroup("item-12", "item-(?<id>\\d+)", "id", null));
    }

    @Test
    void shouldApplyGroupDefaultWithoutReplacingEmptyCapture() {
        assertEquals("fallback", parseGroup("b", "(a)?b", "1", "fallback"));
        assertEquals("fallback", parseGroup("x", "(a)", "1", "fallback"));
        assertEquals("", parseGroup("b", "()b", "1", "fallback"));
        assertNull(parseGroup("x", "(a)", "1", null));
    }

    @Test
    void shouldRejectInvalidGroups() {
        assertNull(parseGroup("a", "(a)", "2", "fallback"));
        assertNull(parseGroup("a", "(?<value>a)", "missing", "fallback"));
        assertNull(parseGroup("a", "(a)", "999999999999999999999", "fallback"));
    }

    @Test
    void shouldReplaceFirstAndAllMatches() {
        assertEquals("a[1]b2", parseReplace("replace-first", "a1b2", "(\\d)", "[$1]", null));
        assertEquals("a[1]b[2]", parseReplace("replace-all", "a1b2", "(\\d)", "[$1]", null));
        assertEquals("abc", parseReplace("replace-all", "abc", "\\d+", "#", null));
        assertEquals("ab", parseReplace("replace-all", "a1b2", "\\d", "", null));
    }

    @Test
    void shouldSupportNamedAndLiteralReplacement() {
        assertEquals("[12]", parseReplace("replace-all", "12", "(?<id>\\d+)", "[${id}]", null));
        assertEquals("$1\\", parseReplace("replace-all", "12", "\\d+", "$1\\", "true"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"i", "m", "s", "u", "U", "x", "d", "l", "c"})
    void shouldAcceptEverySupportedFlag(String flag) {
        assertEquals("true", parse(config(
            "mode", "matches",
            "value", "a",
            "pattern", "a",
            "flags", flag
        )));
    }

    @Test
    void shouldApplyAndCombineFlags() {
        assertEquals("true", parse(config(
            "mode", "matches",
            "value", "A\nB",
            "pattern", "^a.*b$",
            "flags", "iims"
        )));
        assertEquals("true", parse(config(
            "mode", "matches",
            "value", ".+",
            "pattern", ".+",
            "flags", "l"
        )));
        assertNull(parse(config(
            "mode", "matches",
            "value", "a",
            "pattern", "a",
            "flags", "z"
        )));
    }

    @Test
    void shouldAllowEmptyValueAndPattern() {
        assertEquals("true", parse("matches", "", ""));
    }

    @Test
    void shouldRejectInvalidConfiguration() {
        assertNull(parse(config("value", "a", "pattern", "a")));
        assertNull(parse(config("mode", "matches", "pattern", "a")));
        assertNull(parse(config("mode", "matches", "value", "a")));
        assertNull(parse("unknown", "a", "a"));
        assertNull(parse("matches", "a", "["));
        assertNull(parseReplace("replace-all", "a", "(a)", "$2", null));
        assertNull(parseReplace("replace-all", "a", "a", "b", "yes"));
        assertNull(parse(config("mode", "replace-all", "value", "a", "pattern", "a")));
    }

    @Test
    void shouldParseInlineEscapesAndValidateArity() {
        assertEquals("true", parser.parse(context, "matches_a\\_b_a\\_b"));
        assertEquals("true", parser.parse(context, "matches_123_\\d+"));
        assertEquals("a[1]b[2]", parser.parse(context, "replace-all_a1b2_(\\d)_[$1]"));
        assertNull(parser.parse(context, "matches_only-value"));
        assertNull(parser.parse(context, "find_a_a__extra"));
    }

    @Test
    void shouldParseConfigurationValuesThroughManager() {
        assertEquals("true", parse(config(
            "mode", "matches",
            "value", "nested-value",
            "pattern", "nested-value"
        )));
        verify(manager, org.mockito.Mockito.times(2)).parseNullableNode("nested-value", context);
    }

    private String parse(String mode, String value, String pattern) {
        return parse(config("mode", mode, "value", value, "pattern", pattern));
    }

    private String parseGroup(String value, String pattern, String group, String def) {
        return parse(config(
            "mode", "group",
            "value", value,
            "pattern", pattern,
            "group", group,
            "default", def
        ));
    }

    private String parseReplace(String mode, String value, String pattern, String replacement, String literal) {
        return parse(config(
            "mode", mode,
            "value", value,
            "pattern", pattern,
            "replacement", replacement,
            "literal-replacement", literal
        ));
    }

    private String parse(Map<String, Object> params) {
        return parser.parse(context, ConfigReader.parse(params));
    }

    private static Map<String, Object> config(Object... values) {
        val result = new HashMap<String, Object>();
        for (int index = 0; index < values.length; index += 2) {
            if (values[index + 1] != null) result.put(values[index].toString(), values[index + 1]);
        }
        return result;
    }
}
