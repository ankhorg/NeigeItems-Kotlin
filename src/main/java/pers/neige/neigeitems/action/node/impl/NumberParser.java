package pers.neige.neigeitems.action.node.impl;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.node.NodeParser;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.ListUtils;
import pers.neige.neigeitems.utils.ScriptUtils;
import pers.neige.neigeitems.utils.StringUtils;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

public class NumberParser extends NodeParser {
    public NumberParser(@NonNull BaseActionManager manager) {
        super(manager);
    }

    @Override
    public @NonNull String getId() {
        return "number";
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull ConfigReader params
    ) {
        return handle(
            getParsedValue(context, params, "min"),
            getParsedValue(context, params, "max"),
            getParsedValue(context, params, "fixed"),
            getParsedValue(context, params, "mode")
        );
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull String params
    ) {
        val paramsList = StringUtils.split(params, '_', '\\', 4);
        return handle(
            paramsList.get(0),
            ListUtils.getOrNull(paramsList, 1),
            ListUtils.getOrNull(paramsList, 2),
            ListUtils.getOrNull(paramsList, 3)
        );
    }

    private @Nullable String handle(
        @Nullable String min,
        @Nullable String max,
        @Nullable String fixed,
        @Nullable String mode
    ) {
        if (min == null) {
            warning("未指定最小值");
            return null;
        }

        if (max == null) {
            warning("未指定最大值");
            return null;
        }

        val minValue = parse(min, 0D, pers.neige.neigeitems.utils.NumberParser::parseDouble, "{0} 并非数字, 无法用作最小值");
        if (minValue == null) return null;

        val maxValue = parse(max, 0D, pers.neige.neigeitems.utils.NumberParser::parseDouble, "{0} 并非数字, 无法用作最大值");
        if (maxValue == null) return null;

        val result = minValue >= maxValue ? minValue : ThreadLocalRandom.current().nextDouble(minValue, maxValue);

        val fixedValue = parse(fixed, 0, pers.neige.neigeitems.utils.NumberParser::parseInteger, "{0} 并非数字, 无法用作取整位数");
        if (fixedValue == null) return null;

        val roundingMode = ScriptUtils.toRoundingMode(mode);

        return new BigDecimal(Double.toString(result)).setScale(fixedValue, roundingMode).toString();
    }
}
