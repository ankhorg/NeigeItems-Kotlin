package pers.neige.neigeitems.action.evaluator.impl.i64;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.action.evaluator.impl.IntTreeEvaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

public class IntTreeLongEvaluator extends IntTreeEvaluator<Long> {
    public IntTreeLongEvaluator(@NonNull BaseActionManager manager, @NonNull ConfigReader config) {
        super(manager, Long.class, config);
    }

    @Override
    protected @NonNull Evaluator<Long> parseEvaluator(@Nullable Object input) {
        return Evaluator.createLongEvaluator(manager, input);
    }
}
