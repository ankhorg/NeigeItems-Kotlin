package pers.neige.neigeitems.action.evaluator.impl.integer;

import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.impl.RawEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

public class RawIntegerEvaluator extends RawEvaluator<Integer> {
    public RawIntegerEvaluator(@NotNull BaseActionManager manager, @Nullable Integer value) {
        super(manager, Integer.class, value);
    }

    public RawIntegerEvaluator(@NotNull BaseActionManager manager, @Nullable String text) {
        super(manager, Integer.class, text);
    }

    @Override
    public @Nullable Integer cast(@NotNull String result) {
        return StringsKt.toIntOrNull(result);
    }
}
