package pers.neige.neigeitems.action.evaluator.impl;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.SectionUtils;

import java.util.Map;

public abstract class ParseEvaluator<T> extends Evaluator<T> {
    protected final @Nullable String formula;

    public ParseEvaluator(@NotNull BaseActionManager manager, @NotNull Class<T> type, @Nullable String formula) {
        super(manager, type);
        this.formula = formula;
    }

    public @Nullable String getFormula() {
        return formula;
    }

    public abstract @Nullable T cast(@NotNull String result);

    @Override
    @Contract("_, !null -> !null")
    @SuppressWarnings("unchecked")
    public @Nullable T getOrDefault(@NotNull ActionContext context, @Nullable T def) {
        if (formula == null) return def;
        String parseResult = SectionUtils.parseSection(
                formula,
                (Map<String, String>) (Object) context.getGlobal(),
                context.getPlayer(),
                manager.getSectionConfig(context)
        );
        T result = cast(parseResult);
        return result == null ? def : result;
    }
}
