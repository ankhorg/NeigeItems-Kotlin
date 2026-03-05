package pers.neige.neigeitems.action.evaluator.impl.bool;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.action.evaluator.impl.KeyEvaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

public class KeyBooleanEvaluator extends KeyEvaluator<Boolean> {
    public KeyBooleanEvaluator(@NonNull BaseActionManager manager, @NonNull ConfigReader config) {
        super(manager, Boolean.class, config);
    }

    @Override
    protected @NonNull Evaluator<Boolean> parseEvaluator(@Nullable Object input) {
        return Evaluator.createBooleanEvaluator(manager, input);
    }
}
