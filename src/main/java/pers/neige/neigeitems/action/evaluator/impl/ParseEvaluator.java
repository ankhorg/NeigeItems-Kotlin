package pers.neige.neigeitems.action.evaluator.impl;

import lombok.NonNull;
import lombok.ToString;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

@ToString(callSuper = true)
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
    public @Nullable T getOrDefault(@NonNull ActionContext context, @Nullable T def) {
        if (formula == null) return def;
        val parseResult = manager.parseNode(formula, context);
        val result = cast(parseResult);
        return result == null ? def : result;
    }
}
