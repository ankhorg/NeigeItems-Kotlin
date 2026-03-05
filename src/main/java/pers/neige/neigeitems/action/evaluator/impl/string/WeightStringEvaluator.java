package pers.neige.neigeitems.action.evaluator.impl.string;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.action.evaluator.impl.WeightEvaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

public class WeightStringEvaluator extends WeightEvaluator<String> {
    public WeightStringEvaluator(@NonNull BaseActionManager manager, @NonNull ConfigReader config) {
        super(manager, String.class, config);
    }

    @Override
    protected @NonNull Evaluator<String> parseEvaluator(@Nullable Object input) {
        return Evaluator.createStringEvaluator(manager, input);
    }
}
