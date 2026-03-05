package pers.neige.neigeitems.action.evaluator.impl.dbl;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.action.evaluator.impl.ListEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.List;

public class ListDoubleEvaluator extends ListEvaluator<Double> {
    public ListDoubleEvaluator(@NonNull BaseActionManager manager, @NonNull List<?> list) {
        super(manager, Double.class, list);
    }

    @Override
    protected @NonNull Evaluator<Double> parseEvaluator(@Nullable Object input) {
        return Evaluator.createDoubleEvaluator(manager, input);
    }
}
