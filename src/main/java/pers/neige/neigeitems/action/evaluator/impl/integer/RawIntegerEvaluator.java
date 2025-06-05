package pers.neige.neigeitems.action.evaluator.impl.integer;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.impl.RawEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.NumberParser;

public class RawIntegerEvaluator extends RawEvaluator<Integer> {
    public RawIntegerEvaluator(@NonNull BaseActionManager manager, @Nullable Integer value) {
        super(manager, Integer.class, value);
    }

    public RawIntegerEvaluator(@NonNull BaseActionManager manager, @Nullable String text) {
        super(manager, Integer.class, text);
    }

    @Override
    public @Nullable Integer cast(@NonNull String result) {
        return NumberParser.parseInteger(result);
    }
}
