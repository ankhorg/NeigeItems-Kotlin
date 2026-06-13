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
import pers.neige.neigeitems.utils.ScriptUtils;
import pers.neige.neigeitems.utils.StringUtils;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

public class GaussianParser extends NodeParser {
    public GaussianParser(@NonNull BaseActionManager manager) {
        super(manager);
    }

    @Override
    public @NonNull String getId() {
        return "gaussian";
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull ConfigReader params
    ) {
        return handle(
            getParsedValue(context, params, "base"),
            getParsedValue(context, params, "spread"),
            getParsedValue(context, params, "maxSpread"),
            getParsedValue(context, params, "fixed"),
            getParsedValue(context, params, "min"),
            getParsedValue(context, params, "max"),
            getParsedValue(context, params, "mode")
        );
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull String params
    ) {
        val paramsList = StringUtils.split(params, '_', '\\', 7);
        return handle(
            paramsList.get(0),
            ListUtils.getOrNull(paramsList, 1),
            ListUtils.getOrNull(paramsList, 2),
            ListUtils.getOrNull(paramsList, 3),
            ListUtils.getOrNull(paramsList, 4),
            ListUtils.getOrNull(paramsList, 5),
            ListUtils.getOrNull(paramsList, 6)
        );
    }

    private @Nullable String handle(
        @Nullable String base,
        @Nullable String spread,
        @Nullable String maxSpread,
        @Nullable String fixed,
        @Nullable String min,
        @Nullable String max,
        @Nullable String mode
    ) {
        if (base == null) {
            warning("未指定基础值");
            return null;
        }
        if (spread == null) {
            warning("未指定浮动单位");
            return null;
        }
        if (maxSpread == null) {
            warning("未指定浮动范围上限");
            return null;
        }
        val baseValue = parse(base, 0D, NumberParser::parseDouble, "{0} 并非数字, 无法用作基础值");
        if (baseValue == null) return null;
        val spreadValue = parse(spread, 0D, NumberParser::parseDouble, "{0} 并非数字, 无法用作浮动单位");
        if (spreadValue == null) return null;
        val maxSpreadValue = parse(maxSpread, 0D, NumberParser::parseDouble, "{0} 并非数字, 无法用作浮动范围上限");
        if (maxSpreadValue == null) return null;

        double random = ThreadLocalRandom.current().nextGaussian() * spreadValue;
        random = Math.max(-maxSpreadValue, random);
        random = Math.min(maxSpreadValue, random);

        double result = baseValue * (1 + random);

        if (min != null) {
            val minValue = parse(min, 0D, NumberParser::parseDouble, "{0} 并非数字, 无法用作最小值");
            if (minValue == null) return null;
            result = Math.max(result, minValue);
        }

        if (max != null) {
            val maxValue = parse(max, 0D, NumberParser::parseDouble, "{0} 并非数字, 无法用作最大值");
            if (maxValue == null) return null;
            result = Math.min(result, maxValue);
        }

        val fixedValue = parse(fixed, 0, NumberParser::parseInteger, "{0} 并非数字, 无法用作取整位数");
        if (fixedValue == null) return null;

        val roundingMode = ScriptUtils.toRoundingMode(mode);

        return new BigDecimal(Double.toString(result)).setScale(fixedValue, roundingMode).toString();
    }
}
