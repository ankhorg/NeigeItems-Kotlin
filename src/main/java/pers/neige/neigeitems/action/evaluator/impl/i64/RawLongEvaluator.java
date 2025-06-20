package pers.neige.neigeitems.action.evaluator.impl.i64;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.impl.RawEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.NumberParser;

public class RawLongEvaluator extends RawEvaluator<Long> {
    public RawLongEvaluator(@NonNull BaseActionManager manager, @Nullable Long value) {
        super(manager, Long.class, value);
    }

    public RawLongEvaluator(@NonNull BaseActionManager manager, @Nullable String text) {
        super(manager, Long.class, text);
    }

    @Override
    public @Nullable Long cast(@NonNull String result) {
        return NumberParser.parseLong(result);
    }
}
