package pers.neige.neigeitems.action.evaluator.impl.bool;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.impl.ParseEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

public class ParseBooleanEvaluator extends ParseEvaluator<Boolean> {
    public ParseBooleanEvaluator(@NonNull BaseActionManager manager, @Nullable String formula) {
        super(manager, Boolean.class, formula);
    }

    @Override
    public @Nullable Boolean cast(@NonNull String result) {
        return result.equalsIgnoreCase("true");
    }
}
