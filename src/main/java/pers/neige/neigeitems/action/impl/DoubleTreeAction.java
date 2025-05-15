package pers.neige.neigeitems.action.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.NumberParser;

public class DoubleTreeAction extends TreeAction<Double> {
    private final @NotNull Evaluator<Double> key;

    public DoubleTreeAction(@NotNull BaseActionManager manager, @NotNull ConfigReader config) {
        super(manager, config, Double.class);
        this.key = Evaluator.createDoubleEvaluator(manager, config.getString("key"));
    }

    @Override
    public @NotNull ActionType getType() {
        return ActionType.DOUBLE_TREE;
    }

    @Override
    public @Nullable Double cast(@NotNull Object result) {
        return NumberParser.parseDouble(result.toString());
    }

    @Override
    public @NotNull Evaluator<Double> getKey() {
        return key;
    }
}
