package pers.neige.neigeitems.action.evaluator.impl.integer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.impl.RawEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.NumberParser;

public class RawIntegerEvaluator extends RawEvaluator<Integer> {
    public RawIntegerEvaluator(@NotNull BaseActionManager manager, @Nullable Integer value) {
        super(manager, Integer.class, value);
    }

    public RawIntegerEvaluator(@NotNull BaseActionManager manager, @Nullable String text) {
        super(manager, Integer.class, text);
    }

    @Override
    public @Nullable Integer cast(@NotNull String result) {
        return NumberParser.parseInteger(result);
    }
}
