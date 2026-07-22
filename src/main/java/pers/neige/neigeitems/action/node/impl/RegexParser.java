package pers.neige.neigeitems.action.node.impl;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ContextKeys;
import pers.neige.neigeitems.action.node.NodeParser;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.manager.ConfigManager;
import pers.neige.neigeitems.utils.ListUtils;
import pers.neige.neigeitems.utils.NumberParser;
import pers.neige.neigeitems.utils.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexParser extends NodeParser {
    public RegexParser(@NonNull BaseActionManager manager) {
        super(manager);
    }

    @Override
    public @NonNull String getId() {
        return "regex";
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull ConfigReader params
    ) {
        return handle(
            getParsedValue(context, params, "mode"),
            getParsedValue(context, params, "value"),
            getParsedValue(context, params, "pattern"),
            getParsedValue(context, params, "group"),
            getParsedValue(context, params, "default"),
            getParsedValue(context, params, "replacement"),
            getParsedValue(context, params, "flags"),
            getParsedValue(context, params, "literal-replacement")
        );
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull String params
    ) {
        if (context.has(ContextKeys.PAPI_ENVIRONMENT) && !ConfigManager.INSTANCE.getEnableRegexPapi())
            return null;
        val paramsList = StringUtils.split(params, '_', '\\');
        val mode = paramsList.get(0);
        switch (mode) {
            case "matches":
            case "find":
            case "count":
                if (isInvalidSize(paramsList, 3, 4)) return null;
                return handle(
                    mode,
                    paramsList.get(1),
                    paramsList.get(2),
                    null,
                    null,
                    null,
                    ListUtils.getOrNull(paramsList, 3),
                    null
                );
            case "group":
                if (isInvalidSize(paramsList, 3, 6)) return null;
                return handle(
                    mode,
                    paramsList.get(1),
                    paramsList.get(2),
                    ListUtils.getOrNull(paramsList, 3),
                    ListUtils.getOrNull(paramsList, 4),
                    null,
                    ListUtils.getOrNull(paramsList, 5),
                    null
                );
            case "replace-first":
            case "replace-all":
                if (isInvalidSize(paramsList, 4, 6)) return null;
                return handle(
                    mode,
                    paramsList.get(1),
                    paramsList.get(2),
                    null,
                    null,
                    paramsList.get(3),
                    ListUtils.getOrNull(paramsList, 4),
                    ListUtils.getOrNull(paramsList, 5)
                );
            default:
                warning("未知模式: " + mode);
                return null;
        }
    }

    private boolean isInvalidSize(@NonNull List<String> params, int min, int max) {
        if (params.size() >= min && params.size() <= max) return false;
        warning("参数数量应为 " + min + " 至 " + max + " 个, 当前为 " + params.size() + " 个");
        return true;
    }

    private @Nullable String handle(
        @Nullable String mode,
        @Nullable String value,
        @Nullable String pattern,
        @Nullable String group,
        @Nullable String def,
        @Nullable String replacement,
        @Nullable String flags,
        @Nullable String literalReplacement
    ) {
        if (mode == null) {
            warning("缺少 mode 参数");
            return null;
        }
        if (value == null) {
            warning("缺少 value 参数");
            return null;
        }
        if (pattern == null) {
            warning("缺少 pattern 参数");
            return null;
        }

        val patternFlags = parseFlags(flags);
        if (patternFlags == null) return null;

        try {
            val matcher = Pattern.compile(pattern, patternFlags).matcher(value);
            switch (mode) {
                case "matches":
                    return String.valueOf(matcher.matches());
                case "find":
                    return String.valueOf(matcher.find());
                case "count":
                    int count = 0;
                    while (matcher.find()) count++;
                    return String.valueOf(count);
                case "group":
                    if (!matcher.find()) return def;
                    val result = getGroup(matcher, group);
                    return result == null ? def : result;
                case "replace-first":
                    return replace(matcher, replacement, literalReplacement, true);
                case "replace-all":
                    return replace(matcher, replacement, literalReplacement, false);
                default:
                    warning("未知模式: " + mode);
                    return null;
            }
        } catch (PatternSyntaxException exception) {
            warning(exception, "非法正则表达式: " + pattern);
        } catch (IndexOutOfBoundsException | IllegalArgumentException exception) {
            warning(exception, mode + " 模式执行失败");
        }
        return null;
    }

    private @Nullable String getGroup(@NonNull Matcher matcher, @Nullable String group) {
        if (group == null || group.isEmpty()) return matcher.group(0);
        val groupIndex = NumberParser.parseInteger(group);
        if (groupIndex == null) return matcher.group(group);
        return matcher.group(groupIndex);
    }

    private @Nullable String replace(
        @NonNull Matcher matcher,
        @Nullable String replacement,
        @Nullable String literalReplacement,
        boolean first
    ) {
        if (replacement == null) {
            warning("缺少 replacement 参数");
            return null;
        }
        val literal = parseLiteralReplacement(literalReplacement);
        if (literal == null) return null;
        val realReplacement = literal ? Matcher.quoteReplacement(replacement) : replacement;
        return first ? matcher.replaceFirst(realReplacement) : matcher.replaceAll(realReplacement);
    }

    private @Nullable Boolean parseLiteralReplacement(@Nullable String value) {
        if (value == null || value.isEmpty() || "false".equalsIgnoreCase(value)) return false;
        if ("true".equalsIgnoreCase(value)) return true;
        warning("literal-replacement 只能为 true 或 false: " + value);
        return null;
    }

    private @Nullable Integer parseFlags(@Nullable String flags) {
        if (flags == null || flags.isEmpty()) return 0;
        int result = 0;
        for (int index = 0; index < flags.length(); index++) {
            val flag = flags.charAt(index);
            switch (flag) {
                case 'i':
                    result |= Pattern.CASE_INSENSITIVE;
                    break;
                case 'm':
                    result |= Pattern.MULTILINE;
                    break;
                case 's':
                    result |= Pattern.DOTALL;
                    break;
                case 'u':
                    result |= Pattern.UNICODE_CASE;
                    break;
                case 'U':
                    result |= Pattern.UNICODE_CHARACTER_CLASS;
                    break;
                case 'x':
                    result |= Pattern.COMMENTS;
                    break;
                case 'd':
                    result |= Pattern.UNIX_LINES;
                    break;
                case 'l':
                    result |= Pattern.LITERAL;
                    break;
                case 'c':
                    result |= Pattern.CANON_EQ;
                    break;
                default:
                    warning("未知正则标志: " + flag);
                    return null;
            }
        }
        return result;
    }
}
