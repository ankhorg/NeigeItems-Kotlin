package pers.neige.neigeitems.action.evaluator.impl.bool;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.action.evaluator.impl.ListEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.List;

public class ListBooleanEvaluator extends ListEvaluator<Boolean> {
    public ListBooleanEvaluator(@NonNull BaseActionManager manager, @NonNull List<?> list) {
        super(manager, Boolean.class, list);
    }

    @Override
    protected @NonNull Evaluator<Boolean> parseEvaluator(@Nullable Object input) {
        return Evaluator.createBooleanEvaluator(manager, input);
    }
}
