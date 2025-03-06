package pers.neige.neigeitems.action.evaluator.impl.integer;

import kotlin.text.StringsKt;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

public class RawIntegerEvaluator extends Evaluator<Integer> {
    protected final @Nullable Integer value;

    public RawIntegerEvaluator(@NotNull BaseActionManager manager, @Nullable String value) {
        super(manager);
        if (value == null) {
            this.value = null;
        } else {
            this.value = StringsKt.toIntOrNull(value);
        }
    }

    public @Nullable Integer getValue() {
        return value;
    }

    @Override
    @Contract("_, !null -> !null")
    public @Nullable Integer getOrDefault(@NotNull ActionContext context, @Nullable Integer def) {
        return value == null ? def : value;
    }
}
