package pers.neige.neigeitems.action.evaluator.impl;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.NumberParser;

@Getter
@ToString(callSuper = true)
public abstract class IntTreeEvaluator<T> extends TreeEvaluator<Integer, T> {
    private final @NonNull Evaluator<Integer> value;

    public IntTreeEvaluator(
        @NonNull BaseActionManager manager,
        @NonNull Class<T> type,
        @NonNull ConfigReader config
    ) {
        super(manager, type, config, Integer.class);
        this.value = Evaluator.createIntegerEvaluator(manager, config.get("value"));
    }

    @Override
    public @Nullable Integer cast(@NonNull Object result) {
        val intParseResult = NumberParser.parseInteger(result.toString());
        if (intParseResult != null) return intParseResult;
        val doubleParseResult = NumberParser.parseDouble(result.toString());
        if (doubleParseResult != null) return doubleParseResult.intValue();
        return null;
    }
}
