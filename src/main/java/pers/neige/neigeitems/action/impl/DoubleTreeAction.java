package pers.neige.neigeitems.action.impl;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.NumberParser;

public class DoubleTreeAction extends TreeAction<Double> {
    private final @NonNull Evaluator<Double> key;

    public DoubleTreeAction(@NonNull BaseActionManager manager, @NonNull ConfigReader config) {
        super(manager, config, Double.class);
        this.key = Evaluator.createDoubleEvaluator(manager, config.getString("key"));
    }

    @Override
    public @NonNull ActionType getType() {
        return ActionType.DOUBLE_TREE;
    }

    @Override
    public @Nullable Double cast(@NonNull Object result) {
        return NumberParser.parseDouble(result.toString());
    }

    @Override
    public @NonNull Evaluator<Double> getKey() {
        return key;
    }
}
