package pers.neige.neigeitems.action.evaluator.impl.dbl;

import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.impl.ParseEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

public class ParseDoubleEvaluator extends ParseEvaluator<Double> {
    public ParseDoubleEvaluator(@NotNull BaseActionManager manager, @Nullable String formula) {
        super(manager, Double.class, formula);
    }

    @Override
    public @Nullable Double cast(@NotNull String result) {
        return StringsKt.toDoubleOrNull(result);
    }
}
