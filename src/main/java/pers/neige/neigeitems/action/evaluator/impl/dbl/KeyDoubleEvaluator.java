package pers.neige.neigeitems.action.evaluator.impl.dbl;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.action.evaluator.impl.KeyEvaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

public class KeyDoubleEvaluator extends KeyEvaluator<Double> {
    public KeyDoubleEvaluator(@NonNull BaseActionManager manager, @NonNull ConfigReader config) {
        super(manager, Double.class, config);
    }

    @Override
    protected @NonNull Evaluator<Double> parseEvaluator(@Nullable Object input) {
        return Evaluator.createDoubleEvaluator(manager, input);
    }
}
