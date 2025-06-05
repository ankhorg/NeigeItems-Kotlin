package pers.neige.neigeitems.action.evaluator.impl.dbl;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.impl.JsEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

public class JsDoubleEvaluator extends JsEvaluator<Double> {
    public JsDoubleEvaluator(@NonNull BaseActionManager manager, @Nullable String script) {
        super(manager, Double.class, script);
    }

    @Override
    public @Nullable Double cast(@NonNull Object result) {
        return result instanceof Number ? ((Number) result).doubleValue() : null;
    }
}
