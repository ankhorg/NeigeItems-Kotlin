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

import java.text.DecimalFormat;

public class FormatParser extends NodeParser {
    public FormatParser(@NonNull BaseActionManager manager) {
        super(manager);
    }

    @Override
    public @NonNull String getId() {
        return "format";
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull ConfigReader params
    ) {
        return handle(
            getParsedValue(context, params, "value"),
            getParsedValue(context, params, "format"),
            getParsedValue(context, params, "mode")
        );
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull String params
    ) {
        val paramsList = StringUtils.split(params, '_', '\\', 3);
        return handle(
            paramsList.get(0),
            ListUtils.getOrNull(paramsList, 1),
            ListUtils.getOrNull(paramsList, 2)
        );
    }

    private @Nullable String handle(
        @Nullable String value,
        @Nullable String format,
        @Nullable String mode
    ) {
        val valueNumber = parse(value, 0D, NumberParser::parseDouble, "{0} 并非数字, 无法用作待操作的数字");
        if (valueNumber == null) return null;
        val decimalFormat = new DecimalFormat(format == null ? "#.#" : format);
        decimalFormat.setRoundingMode(ScriptUtils.toRoundingMode(mode));
        return decimalFormat.format(value);
    }
}
