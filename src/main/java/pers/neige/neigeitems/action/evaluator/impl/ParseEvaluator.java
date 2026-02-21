package pers.neige.neigeitems.action.evaluator.impl;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.SectionUtils;

import java.util.Map;

public abstract class ParseEvaluator<T> extends Evaluator<T> {
    protected final @Nullable String formula;

    public ParseEvaluator(@NonNull BaseActionManager manager, @NonNull Class<T> type, @Nullable String formula) {
        super(manager, type);
        this.formula = formula;
    }

    public @Nullable String getFormula() {
        return formula;
    }

    public abstract @Nullable T cast(@NonNull String result);

    @Override
    @Contract("_, !null -> !null")
    @SuppressWarnings("unchecked")
    public @Nullable T getOrDefault(@NonNull ActionContext context, @Nullable T def) {
        if (formula == null) return def;
        val parseResult = SectionUtils.parseSection(
            formula,
            (Map<String, String>) (Object) context.getGlobal(),
            context.getPlayer(),
            manager.getSectionConfig(context)
        );
        val result = cast(parseResult);
        return result == null ? def : result;
    }
}
