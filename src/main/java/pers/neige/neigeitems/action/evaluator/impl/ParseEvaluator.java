package pers.neige.neigeitems.action.evaluator.impl;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.action.evaluator.EvaluatorConverter;
import pers.neige.neigeitems.manager.BaseActionManager;

@Getter
@ToString(callSuper = true)
public class ParseEvaluator<T> extends Evaluator<T> {
    private final @Nullable String formula;
    private final @NonNull EvaluatorConverter<T> converter;

    public ParseEvaluator(@NonNull BaseActionManager manager, @NonNull Class<T> type, @Nullable String formula) {
        this(manager, type, formula, new EvaluatorConverter<>(type));
    }

    public ParseEvaluator(
        @NonNull BaseActionManager manager,
        @NonNull Class<T> type,
        @Nullable String formula,
        @NonNull EvaluatorConverter<T> converter
    ) {
        super(manager, type);
        this.formula = formula;
        this.converter = converter;
    }

    @Override
    @Contract("_, !null -> !null")
    public @Nullable T getOrDefault(@NonNull ActionContext context, @Nullable T def) {
        if (formula == null) return def;
        val parseResult = manager.parseNode(formula, context);
        val result = converter.convert(parseResult);
        return result == null ? def : result;
    }
}
