package pers.neige.neigeitems.action.impl;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.NumberParser;

public class IntTreeAction extends TreeAction<Integer> {
    private final @NonNull Evaluator<Integer> key;

    public IntTreeAction(@NonNull BaseActionManager manager, @NonNull ConfigReader config) {
        super(manager, config, Integer.class);
        this.key = Evaluator.createIntegerEvaluator(manager, config.getString("key"));
    }

    @Override
    public @NonNull ActionType getType() {
        return ActionType.INT_TREE;
    }

    @Override
    public @Nullable Integer cast(@NonNull Object result) {
        val intParseResult = NumberParser.parseInteger(result.toString());
        if (intParseResult != null) return intParseResult;
        val doubleParseResult = NumberParser.parseDouble(result.toString());
        if (doubleParseResult != null) return doubleParseResult.intValue();
        return null;
    }

    @Override
    public @NonNull Evaluator<Integer> getKey() {
        return key;
    }
}
