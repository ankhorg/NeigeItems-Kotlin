package pers.neige.neigeitems.action.evaluator.impl.integer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.impl.JsEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

public class JsIntegerEvaluator extends JsEvaluator<Integer> {
    public JsIntegerEvaluator(@NotNull BaseActionManager manager, @Nullable String script) {
        super(manager, Integer.class, script);
    }

    @Override
    public @Nullable Integer cast(@NotNull Object result) {
        return result instanceof Number ? ((Number) result).intValue() : null;
    }
}
