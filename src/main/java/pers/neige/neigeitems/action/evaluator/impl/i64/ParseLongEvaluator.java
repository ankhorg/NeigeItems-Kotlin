package pers.neige.neigeitems.action.evaluator.impl.i64;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.impl.ParseEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.NumberParser;

public class ParseLongEvaluator extends ParseEvaluator<Long> {
    public ParseLongEvaluator(@NonNull BaseActionManager manager, @Nullable String formula) {
        super(manager, Long.class, formula);
    }

    @Override
    public @Nullable Long cast(@NonNull String result) {
        return NumberParser.parseLong(result);
    }
}
