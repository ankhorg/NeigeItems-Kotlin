package pers.neige.neigeitems.action.node.impl;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.node.NodeParser;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.ListUtils;
import pers.neige.neigeitems.utils.NumberParser;
import pers.neige.neigeitems.utils.StringUtils;

import java.util.List;
import java.util.Locale;

public class StringParser extends NodeParser {
    public StringParser(@NonNull BaseActionManager manager) {
        super(manager);
    }

    @Override
    public @NonNull String getId() {
        return "string";
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull ConfigReader params
    ) {
        return handle(
            getParsedValue(context, params, "mode"),
            getParsedValue(context, params, "value"),
            getParsedValue(context, params, "arg"),
            getParsedValue(context, params, "start"),
            getParsedValue(context, params, "end"),
            getParsedValue(context, params, "default"),
            getParsedValue(context, params, "target"),
            getParsedValue(context, params, "replacement")
        );
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull String params
    ) {
        val paramsList = StringUtils.split(params, '_', '\\');
        if (paramsList.isEmpty()) {
            warning("缺少 mode 参数");
            return null;
        }
        val mode = paramsList.get(0);
        switch (mode) {
            case "lower":
            case "upper":
            case "trim":
            case "length":
                if (isInvalidSize(paramsList, 2, 2)) return null;
                return handle(mode, paramsList.get(1), null, null, null, null, null, null);
            case "contains":
            case "starts-with":
            case "ends-with":
                if (isInvalidSize(paramsList, 3, 3)) return null;
                return handle(mode, paramsList.get(1), paramsList.get(2), null, null, null, null, null);
            case "substring":
                if (isInvalidSize(paramsList, 3, 5)) return null;
                return handle(
                    mode,
                    paramsList.get(1),
                    null,
                    paramsList.get(2),
                    ListUtils.getOrNull(paramsList, 3),
                    ListUtils.getOrNull(paramsList, 4),
                    null,
                    null
                );
            case "replace-literal":
                if (isInvalidSize(paramsList, 4, 4)) return null;
                return handle(
                    mode,
                    paramsList.get(1),
                    null,
                    null,
                    null,
                    null,
                    paramsList.get(2),
                    paramsList.get(3)
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
        @Nullable String arg,
        @Nullable String start,
        @Nullable String end,
        @Nullable String def,
        @Nullable String target,
        @Nullable String replacement
    ) {
        if (mode == null) {
            warning("缺少 mode 参数");
            return null;
        }
        if (value == null) {
            warning("缺少 value 参数");
            return null;
        }
        switch (mode) {
            case "lower":
                return value.toLowerCase(Locale.ROOT);
            case "upper":
                return value.toUpperCase(Locale.ROOT);
            case "trim":
                return value.trim();
            case "length":
                return Integer.toString(value.length());
            case "contains":
                if (arg == null) {
                    warning("缺少 arg 参数");
                    return null;
                }
                return Boolean.toString(value.contains(arg));
            case "starts-with":
                if (arg == null) {
                    warning("缺少 arg 参数");
                    return null;
                }
                return Boolean.toString(value.startsWith(arg));
            case "ends-with":
                if (arg == null) {
                    warning("缺少 arg 参数");
                    return null;
                }
                return Boolean.toString(value.endsWith(arg));
            case "substring":
                return substring(value, start, end, def);
            case "replace-literal":
                if (target == null) {
                    warning("缺少 target 参数");
                    return null;
                }
                if (replacement == null) {
                    warning("缺少 replacement 参数");
                    return null;
                }
                return value.replace(target, replacement);
            default:
                warning("未知模式: " + mode);
                return null;
        }
    }

    private @Nullable String substring(
        @NonNull String value,
        @Nullable String startText,
        @Nullable String endText,
        @Nullable String def
    ) {
        if (startText == null) {
            warning("缺少 start 参数");
            return null;
        }
        val start = parse(startText, 0, NumberParser::parseInteger, "{0} 并非整数, 无法用作 substring 起始索引");
        if (start == null) return null;
        Integer end;
        if (endText == null || endText.isEmpty()) {
            end = value.length();
        } else {
            end = parse(endText, 0, NumberParser::parseInteger, "{0} 并非整数, 无法用作 substring 结束索引");
            if (end == null) return null;
        }
        val length = value.length();
        val from = Math.max(0, Math.min(start, length));
        val to = Math.max(from, Math.min(end, length));
        if (start < 0 || end < 0 || start > length || end > length) {
            return def;
        }
        return value.substring(from, to);
    }
}
