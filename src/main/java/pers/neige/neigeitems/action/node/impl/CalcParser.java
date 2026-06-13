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
import java.text.MessageFormat;

public abstract class CalcParser extends NodeParser {
    public CalcParser(@NonNull BaseActionManager manager) {
        super(manager);
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull ConfigReader params
    ) {
        return handle(
            getParsedValue(context, params, "formula"),
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
        val paramsList = StringUtils.split(params, '_', '\\', 5);
        return handle(
            paramsList.get(0),
            ListUtils.getOrNull(paramsList, 1),
            ListUtils.getOrNull(paramsList, 2),
            ListUtils.getOrNull(paramsList, 3),
            ListUtils.getOrNull(paramsList, 4)
        );
    }

    private @Nullable String handle(
        @Nullable String formula,
        @Nullable String fixed,
        @Nullable String min,
        @Nullable String max,
        @Nullable String mode
    ) {
        if (formula == null) {
            warning("未指定计算公式");
            return null;
        }
        try {
            double result = calc(formula);

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
        } catch (Throwable throwable) {
            warning(throwable, "解析公式 " + formula + " 时出错");
            return null;
        }
    }

    protected abstract double calc(@NonNull String formula);
}
