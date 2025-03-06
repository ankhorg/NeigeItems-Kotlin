package pers.neige.neigeitems.action.evaluator.impl.dbl;

import kotlin.text.StringsKt;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

public class RawDoubleEvaluator extends Evaluator<Double> {
    protected final @Nullable Double value;

    public RawDoubleEvaluator(@NotNull BaseActionManager manager, @Nullable String value) {
        super(manager);
        if (value == null) {
            this.value = null;
        } else {
            this.value = StringsKt.toDoubleOrNull(value);
        }
    }

    public @Nullable Double getValue() {
        return value;
    }

    @Override
    @Contract("_, !null -> !null")
    public @Nullable Double getOrDefault(@NotNull ActionContext context, @Nullable Double def) {
        return value == null ? def : value;
    }
}
