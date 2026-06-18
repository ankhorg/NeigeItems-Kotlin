package pers.neige.neigeitems.action.evaluator.impl.list;

import lombok.NonNull;
import lombok.ToString;
import lombok.val;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.Collections;
import java.util.List;

@ToString(callSuper = true)
public class ListWrappedEvaluator<T> extends Evaluator<List<T>> {
    private final @NonNull Evaluator<T> evaluator;

    public ListWrappedEvaluator(
        @NonNull BaseActionManager manager,
        @NonNull Class<List<T>> type,
        @NonNull Evaluator<T> evaluator
    ) {
        super(manager, type);
        this.evaluator = evaluator;
    }

    @Override
    @Contract("_, !null -> !null")
    public @Nullable List<T> getOrDefault(@NonNull ActionContext context, @Nullable List<T> def) {
        val result = evaluator.get(context);
        return result == null ? def : Collections.singletonList(result);
    }
}
