package pers.neige.neigeitems.action.evaluator.impl.dbl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.impl.JsEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

public class JsDoubleEvaluator extends JsEvaluator<Double> {
    public JsDoubleEvaluator(@NotNull BaseActionManager manager, @Nullable String script) {
        super(manager, Double.class, script);
    }

    @Override
    public @Nullable Double cast(@NotNull Object result) {
        return result instanceof Number ? ((Number) result).doubleValue() : null;
    }
}
