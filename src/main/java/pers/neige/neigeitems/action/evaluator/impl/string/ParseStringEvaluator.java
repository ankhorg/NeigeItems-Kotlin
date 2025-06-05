package pers.neige.neigeitems.action.evaluator.impl.string;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.impl.ParseEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

public class ParseStringEvaluator extends ParseEvaluator<String> {
    public ParseStringEvaluator(@NonNull BaseActionManager manager, @Nullable String formula) {
        super(manager, String.class, formula);
    }

    @Override
    public @Nullable String cast(@NonNull String result) {
        return result;
    }
}
