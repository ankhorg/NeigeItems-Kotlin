package pers.neige.neigeitems.action.evaluator.impl;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.action.evaluator.EvaluatorConverter;
import pers.neige.neigeitems.manager.BaseActionManager;

@Getter
@ToString(callSuper = true)
public class RawEvaluator<T> extends Evaluator<T> {
    private final @Nullable T value;

    public RawEvaluator(@NonNull BaseActionManager manager, @NonNull Class<T> type, @Nullable T value) {
        super(manager, type);
        this.value = value;
    }

    public RawEvaluator(@NonNull BaseActionManager manager, @NonNull Class<T> type, @Nullable String text) {
        this(manager, type, text, new EvaluatorConverter<>(type));
    }

    public RawEvaluator(
        @NonNull BaseActionManager manager,
        @NonNull Class<T> type,
        @Nullable String text,
        @NonNull EvaluatorConverter<T> converter
    ) {
        super(manager, type);
        this.value = text == null ? null : converter.convert(text);
    }

    public @Nullable T getValue() {
        return value;
    }

    @Override
    @Contract("_, !null -> !null")
    public @Nullable T getOrDefault(@NonNull ActionContext context, @Nullable T def) {
        return value == null ? def : value;
    }
}
