package pers.neige.neigeitems.action.evaluator.impl.i64;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.impl.JsEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

public class JsLongEvaluator extends JsEvaluator<Long> {
    public JsLongEvaluator(@NonNull BaseActionManager manager, @Nullable String script) {
        super(manager, Long.class, script);
    }

    @Override
    public @Nullable Long cast(@NonNull Object result) {
        return result instanceof Number ? ((Number) result).longValue() : null;
    }
}
