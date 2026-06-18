package pers.neige.neigeitems.action.evaluator.impl.list;

import lombok.NonNull;
import lombok.ToString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.Collections;
import java.util.List;

@ToString(callSuper = true)
public class NullListElementEvaluator<T> extends Evaluator<List<T>> {
    public NullListElementEvaluator(
        @NonNull BaseActionManager manager,
        @NonNull Class<List<T>> type
    ) {
        super(manager, type);
    }

    @Override
    @Contract("_, !null -> !null")
    public @Nullable List<T> getOrDefault(@NonNull ActionContext context, @Nullable List<T> def) {
        return Collections.singletonList(null);
    }
}
