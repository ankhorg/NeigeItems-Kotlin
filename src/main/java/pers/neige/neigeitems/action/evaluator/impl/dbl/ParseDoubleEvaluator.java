package pers.neige.neigeitems.action.evaluator.impl.dbl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.impl.ParseEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.NumberParser;

public class ParseDoubleEvaluator extends ParseEvaluator<Double> {
    public ParseDoubleEvaluator(@NotNull BaseActionManager manager, @Nullable String formula) {
        super(manager, Double.class, formula);
    }

    @Override
    public @Nullable Double cast(@NotNull String result) {
        return NumberParser.parseDouble(result);
    }
}
