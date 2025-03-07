package pers.neige.neigeitems.action.evaluator.impl.string;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

public class RawStringEvaluator extends Evaluator<String> {
    protected final @Nullable String value;

    public RawStringEvaluator(@NotNull BaseActionManager manager, @Nullable String value) {
        super(manager, String.class);
        this.value = value;
    }

    public @Nullable String getValue() {
        return value;
    }

    @Override
    @Contract("_, !null -> !null")
    public @Nullable String getOrDefault(@NotNull ActionContext context, @Nullable String def) {
        return value == null ? def : value;
    }
}
