package pers.neige.neigeitems.action.impl;

import kotlin.text.StringsKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

public class IntTreeAction extends TreeAction<Integer> {
    private final Evaluator<Integer> key;

    public IntTreeAction(@NotNull BaseActionManager manager, @NotNull ConfigReader config) {
        super(manager, config, Integer.class);
        this.key = Evaluator.createIntegerEvaluator(manager, config.getString("key", ""));
    }

    @Override
    public @NotNull ActionType getType() {
        return ActionType.INT_TREE;
    }

    @Override
    public @Nullable Integer cast(@NotNull Object result) {
        Integer intParseResult = StringsKt.toIntOrNull(result.toString());
        if (intParseResult != null) return intParseResult;
        Double doubleParseResult = StringsKt.toDoubleOrNull(result.toString());
        if (doubleParseResult != null) return doubleParseResult.intValue();
        return null;
    }

    @Override
    public @NotNull Evaluator<Integer> getKey() {
        return key;
    }
}
