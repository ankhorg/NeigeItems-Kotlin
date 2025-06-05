package pers.neige.neigeitems.action.evaluator.impl.dbl;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.impl.RawEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.NumberParser;

public class RawDoubleEvaluator extends RawEvaluator<Double> {
    public RawDoubleEvaluator(@NonNull BaseActionManager manager, @Nullable Double value) {
        super(manager, Double.class, value);
    }

    public RawDoubleEvaluator(@NonNull BaseActionManager manager, @Nullable String text) {
        super(manager, Double.class, text);
    }

    @Override
    public @Nullable Double cast(@NonNull String result) {
        return NumberParser.parseDouble(result);
    }
}
