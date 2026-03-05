package pers.neige.neigeitems.action.evaluator.impl;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString(callSuper = true)
public abstract class ListEvaluator<T> extends Evaluator<T> {
    private final @NonNull List<Evaluator<T>> evaluators = new ArrayList<>();

    public ListEvaluator(
        @NonNull BaseActionManager manager,
        @NonNull Class<T> type,
        @NonNull List<?> list
    ) {
        super(manager, type);
        for (val element : list) {
            evaluators.add(parseEvaluator(element));
        }
    }

    protected abstract @NonNull Evaluator<T> parseEvaluator(@Nullable Object input);

    @Override
    @Contract("_, !null -> !null")
    public @Nullable T getOrDefault(@NonNull ActionContext context, @Nullable T def) {
        for (val evaluator : evaluators) {
            val result = evaluator.get(context);
            if (result != null) return result;
        }
        return def;
    }
}
