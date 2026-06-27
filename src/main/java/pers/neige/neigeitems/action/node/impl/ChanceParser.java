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

import java.util.concurrent.ThreadLocalRandom;

public class ChanceParser extends NodeParser {
    public ChanceParser(@NonNull BaseActionManager manager) {
        super(manager);
    }

    @Override
    public @NonNull String getId() {
        return "chance";
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull ConfigReader params
    ) {
        return handle(
            getParsedValue(context, params, "success"),
            getParsedValue(context, params, "total"),
            getParsedValue(context, params, "repeat"),
            getParsedValue(context, params, "min"),
            getParsedValue(context, params, "max")
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
        @Nullable String success,
        @Nullable String total,
        @Nullable String repeat,
        @Nullable String min,
        @Nullable String max
    ) {
        if (success == null) {
            warning("未指定成功概率");
            return null;
        }

        val successRate = parse(success, 0D, NumberParser::parseDouble, "{0} 并非数字, 无法用作成功概率");
        if (successRate == null) return null;

        val totalRate = parse(total, 1D, NumberParser::parseDouble, "{0} 并非数字, 无法用作总概率");
        if (totalRate == null) return null;

        val repeatTimes = parse(repeat, 1, NumberParser::parseInteger, "{0} 并非整数, 无法用作重复次数");
        if (repeatTimes == null) return null;

        int result = 0;
        for (int i = 0; i < repeatTimes; i++) {
            if (successRate > ThreadLocalRandom.current().nextDouble(0D, totalRate)) {
                result++;
            }
        }

        if (min != null && !min.isEmpty()) {
            val minValue = parse(min, 0, NumberParser::parseInteger, "{0} 并非整数, 无法用作最小值");
            if (minValue == null) return null;
            result = Math.max(result, minValue);
        }

        if (max != null && !max.isEmpty()) {
            val maxValue = parse(max, 0, NumberParser::parseInteger, "{0} 并非整数, 无法用作最大值");
            if (maxValue == null) return null;
            result = Math.min(result, maxValue);
        }

        return Integer.toString(result);
    }
}
