package pers.neige.neigeitems.action.evaluator.impl.i64;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.action.evaluator.impl.ListEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.List;

public class ListLongEvaluator extends ListEvaluator<Long> {
    public ListLongEvaluator(@NonNull BaseActionManager manager, @NonNull List<?> list) {
        super(manager, Long.class, list);
    }

    @Override
    protected @NonNull Evaluator<Long> parseEvaluator(@Nullable Object input) {
        return Evaluator.createLongEvaluator(manager, input);
    }
}
