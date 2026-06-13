package pers.neige.neigeitems.action.node.impl;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ContextKeys;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.manager.ConfigManager;
import pers.neige.neigeitems.utils.ScriptUtils;

public class CalculationParser extends CalcParser {
    public CalculationParser(@NonNull BaseActionManager manager) {
        super(manager);
    }

    @Override
    public @NonNull String getId() {
        return "calculation";
    }

    @Override
    public @Nullable String parse(@NonNull ActionContext context, @NonNull String params) {
        if (context.has(ContextKeys.PAPI_ENVIRONMENT) && !ConfigManager.INSTANCE.getEnableJsPapi())
            return null;
        return super.parse(context, params);
    }

    @Override
    protected double calc(@NonNull String formula) {
        return ScriptUtils.calculate(formula);
    }
}
