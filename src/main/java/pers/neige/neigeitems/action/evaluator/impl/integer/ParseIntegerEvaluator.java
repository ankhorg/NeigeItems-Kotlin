package pers.neige.neigeitems.action.evaluator.impl.integer;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.impl.ParseEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.NumberParser;

public class ParseIntegerEvaluator extends ParseEvaluator<Integer> {
    public ParseIntegerEvaluator(@NonNull BaseActionManager manager, @Nullable String formula) {
        super(manager, Integer.class, formula);
    }

    @Override
    public @Nullable Integer cast(@NonNull String result) {
        return NumberParser.parseInteger(result);
    }
}
