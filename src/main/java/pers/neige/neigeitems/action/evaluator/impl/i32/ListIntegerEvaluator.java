package pers.neige.neigeitems.action.evaluator.impl.i32;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.action.evaluator.impl.ListEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.List;

public class ListIntegerEvaluator extends ListEvaluator<Integer> {
    public ListIntegerEvaluator(@NonNull BaseActionManager manager, @NonNull List<?> list) {
        super(manager, Integer.class, list);
    }

    @Override
    protected @NonNull Evaluator<Integer> parseEvaluator(@Nullable Object input) {
        return Evaluator.createIntegerEvaluator(manager, input);
    }
}
