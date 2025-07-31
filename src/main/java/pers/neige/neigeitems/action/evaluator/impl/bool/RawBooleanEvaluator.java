package pers.neige.neigeitems.action.evaluator.impl.bool;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.impl.RawEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

public class RawBooleanEvaluator extends RawEvaluator<Boolean> {
    public RawBooleanEvaluator(@NonNull BaseActionManager manager, @Nullable Boolean value) {
        super(manager, Boolean.class, value);
    }

    public RawBooleanEvaluator(@NonNull BaseActionManager manager, @Nullable String text) {
        super(manager, Boolean.class, text);
    }

    @Override
    public @Nullable Boolean cast(@NonNull String result) {
        if (result.equalsIgnoreCase("true")) {
            return true;
        } else if (result.equalsIgnoreCase("false")) {
            return false;
        } else {
            return null;
        }
    }
}
