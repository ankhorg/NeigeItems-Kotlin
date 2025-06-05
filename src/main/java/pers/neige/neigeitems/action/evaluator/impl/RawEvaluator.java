package pers.neige.neigeitems.action.evaluator.impl;

import lombok.NonNull;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

public abstract class RawEvaluator<T> extends Evaluator<T> {
    protected final @Nullable T value;

    public RawEvaluator(@NonNull BaseActionManager manager, @NonNull Class<T> type, @Nullable T value) {
        super(manager, type);
        this.value = value;
    }

    public RawEvaluator(@NonNull BaseActionManager manager, @NonNull Class<T> type, @Nullable String text) {
        super(manager, type);
        this.value = text == null ? null : cast(text);
    }

    public @Nullable T getValue() {
        return value;
    }

    public abstract @Nullable T cast(@NonNull String result);

    @Override
    @Contract("_, !null -> !null")
    public @Nullable T getOrDefault(@NonNull ActionContext context, @Nullable T def) {
        return value == null ? def : value;
    }
}
