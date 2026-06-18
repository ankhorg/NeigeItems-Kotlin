package pers.neige.neigeitems.action.node.impl;

import kotlin.text.StringsKt;
import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.node.NodeParser;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.ColorUtils;
import pers.neige.neigeitems.utils.ListUtils;
import pers.neige.neigeitems.utils.NumberParser;
import pers.neige.neigeitems.utils.StringUtils;

public class GradientParser extends NodeParser {
    public GradientParser(@NonNull BaseActionManager manager) {
        super(manager);
    }

    @Override
    public @NonNull String getId() {
        return "gradient";
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull ConfigReader params
    ) {
        return handle(
            getParsedValue(context, params, "colorStart"),
            getParsedValue(context, params, "colorEnd"),
            getParsedValue(context, params, "step"),
            getParsedValue(context, params, "text")
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
        @Nullable String colorStartString,
        @Nullable String colorEndString,
        @Nullable String stepString,
        @Nullable String text
    ) {
        if (colorStartString == null) {
            warning("未指定起始颜色");
            return null;
        }
        if (colorEndString == null) {
            warning("未指定终止颜色");
            return null;
        }
        if (text == null) {
            warning("未指定待处理文本");
            return null;
        }
        Integer colorStart = parse(colorStartString, 0, (it) -> StringsKt.toIntOrNull(it, 16), "{0} 并非数字, 无法用作起始颜色");
        if (colorStart == null) return null;
        colorStart = Math.min(Math.max(colorStart, 0), 0xFFFFFF);
        Integer colorEnd = parse(colorEndString, 0, (it) -> StringsKt.toIntOrNull(it, 16), "{0} 并非数字, 无法用作终止颜色");
        if (colorEnd == null) return null;
        colorEnd = Math.min(Math.max(colorEnd, 0), 0xFFFFFF);
        val step = parse(stepString, 1, NumberParser::parseInteger, "{0} 并非数字, 无法用作步长");
        if (step == null) return null;

        if (text.length() <= step) {

            return ColorUtils.toHexColorPrefix(colorStart) + text;
        }

        val chars = text.toCharArray();
        val result = new StringBuilder();

        int redCurrent = ColorUtils.getRed(colorStart);
        int greenCurrent = ColorUtils.getGreen(colorStart);
        int blueCurrent = ColorUtils.getBlue(colorStart);

        if (step == 1) {
            val redStep = (ColorUtils.getRed(colorEnd) - redCurrent) / (chars.length - 1);
            val greenStep = (ColorUtils.getGreen(colorEnd) - greenCurrent) / (chars.length - 1);
            val blueStep = (ColorUtils.getBlue(colorEnd) - blueCurrent) / (chars.length - 1);

            for (val c : chars) {
                result.append(ColorUtils.toHexColorPrefix(redCurrent, greenCurrent, blueCurrent));
                result.append(c);
                redCurrent += redStep;
                greenCurrent += greenStep;
                blueCurrent += blueStep;
            }
        } else {
            val redStep = (ColorUtils.getRed(colorEnd) - redCurrent) * step / (chars.length - 1);
            val greenStep = (ColorUtils.getGreen(colorEnd) - greenCurrent) * step / (chars.length - 1);
            val blueStep = (ColorUtils.getBlue(colorEnd) - blueCurrent) * step / (chars.length - 1);

            int current = 1;

            for (val c : chars) {
                if (current == 1) {
                    result.append(ColorUtils.toHexColorPrefix(redCurrent, greenCurrent, blueCurrent));
                    redCurrent += redStep;
                    greenCurrent += greenStep;
                    blueCurrent += blueStep;
                }
                result.append(c);
                if (current == step) {
                    current = 1;
                } else {
                    current++;
                }
            }
        }
        return result.toString();
    }
}
