package pers.neige.neigeitems.action.evaluator.impl.i32;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.action.evaluator.impl.WeightEvaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

public class WeightIntegerEvaluator extends WeightEvaluator<Integer> {
    public WeightIntegerEvaluator(@NonNull BaseActionManager manager, @NonNull ConfigReader config) {
        super(manager, Integer.class, config);
    }

    @Override
    protected @NonNull Evaluator<Integer> parseEvaluator(@Nullable Object input) {
        return Evaluator.createIntegerEvaluator(manager, input);
    }
}
