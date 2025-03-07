package pers.neige.neigeitems.action.evaluator.impl.string;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.impl.ParseEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

public class ParseStringEvaluator extends ParseEvaluator<String> {
    public ParseStringEvaluator(@NotNull BaseActionManager manager, @Nullable String formula) {
        super(manager, String.class, formula);
    }

    @Override
    public @Nullable String cast(@NotNull String result) {
        return result;
    }
}
