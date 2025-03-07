package pers.neige.neigeitems.action.evaluator.impl.integer;

import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.impl.ParseEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

public class ParseIntegerEvaluator extends ParseEvaluator<Integer> {
    public ParseIntegerEvaluator(@NotNull BaseActionManager manager, @Nullable String formula) {
        super(manager, Integer.class, formula);
    }

    @Override
    public @Nullable Integer cast(@NotNull String result) {
        return StringsKt.toIntOrNull(result);
    }
}
