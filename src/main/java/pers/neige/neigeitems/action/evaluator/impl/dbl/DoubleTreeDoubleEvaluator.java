package pers.neige.neigeitems.action.evaluator.impl.dbl;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.action.evaluator.impl.DoubleTreeEvaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

public class DoubleTreeDoubleEvaluator extends DoubleTreeEvaluator<Double> {
    public DoubleTreeDoubleEvaluator(@NonNull BaseActionManager manager, @NonNull ConfigReader config) {
        super(manager, Double.class, config);
    }

    @Override
    protected @NonNull Evaluator<Double> parseEvaluator(@Nullable Object input) {
        return Evaluator.createDoubleEvaluator(manager, input);
    }
}
