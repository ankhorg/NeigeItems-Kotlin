package pers.neige.neigeitems.action.evaluator.impl;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.NumberParser;

@Getter
@ToString(callSuper = true)
public abstract class DoubleTreeEvaluator<T> extends TreeEvaluator<Double, T> {
    private final @NonNull Evaluator<Double> value;

    public DoubleTreeEvaluator(
        @NonNull BaseActionManager manager,
        @NonNull Class<T> type,
        @NonNull ConfigReader config
    ) {
        super(manager, type, config, Double.class);
        this.value = Evaluator.createDoubleEvaluator(manager, config.get("value"));
    }

    @Override
    public @Nullable Double cast(@NonNull Object result) {
        return NumberParser.parseDouble(result.toString());
    }
}
