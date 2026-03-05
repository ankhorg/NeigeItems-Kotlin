package pers.neige.neigeitems.action.evaluator.impl;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.Condition;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

@Getter
@ToString(callSuper = true)
public abstract class ConditionEvaluator<T> extends Evaluator<T> {
    protected final @NonNull Condition condition;
    protected final @NonNull Evaluator<T> thenEvaluator;
    protected final @NonNull Evaluator<T> elseEvaluator;

    public ConditionEvaluator(
        @NonNull BaseActionManager manager,
        @NonNull Class<T> type,
        @NonNull ConfigReader config
    ) {
        super(manager, type);
        this.condition = new Condition(manager, config.getString("condition"));
        this.thenEvaluator = parseEvaluator(config.get("then"));
        this.elseEvaluator = parseEvaluator(config.get("else"));
    }

    protected abstract @NonNull Evaluator<T> parseEvaluator(@Nullable Object input);

    @Override
    @Contract("_, !null -> !null")
    public @Nullable T getOrDefault(@NonNull ActionContext context, @Nullable T def) {
        if (this.condition.easyCheck(context)) {
            return this.thenEvaluator.getOrDefault(context, def);
        } else {
            return this.elseEvaluator.getOrDefault(context, def);
        }
    }
}
