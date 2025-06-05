package pers.neige.neigeitems.action.evaluator.impl.dbl;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.impl.ParseEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.NumberParser;

public class ParseDoubleEvaluator extends ParseEvaluator<Double> {
    public ParseDoubleEvaluator(@NonNull BaseActionManager manager, @Nullable String formula) {
        super(manager, Double.class, formula);
    }

    @Override
    public @Nullable Double cast(@NonNull String result) {
        return NumberParser.parseDouble(result);
    }
}
