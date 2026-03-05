package pers.neige.neigeitems.action.evaluator.impl.i32;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.action.evaluator.impl.KeyEvaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

public class KeyIntegerEvaluator extends KeyEvaluator<Integer> {
    public KeyIntegerEvaluator(@NonNull BaseActionManager manager, @NonNull ConfigReader config) {
        super(manager, Integer.class, config);
    }

    @Override
    protected @NonNull Evaluator<Integer> parseEvaluator(@Nullable Object input) {
        return Evaluator.createIntegerEvaluator(manager, input);
    }
}
