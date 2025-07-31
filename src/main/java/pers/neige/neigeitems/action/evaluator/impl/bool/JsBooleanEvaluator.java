package pers.neige.neigeitems.action.evaluator.impl.bool;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.impl.JsEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

public class JsBooleanEvaluator extends JsEvaluator<Boolean> {
    public JsBooleanEvaluator(@NonNull BaseActionManager manager, @Nullable String script) {
        super(manager, Boolean.class, script);
    }

    @Override
    public @Nullable Boolean cast(@NonNull Object result) {
        return result instanceof Boolean ? (Boolean) result : null;
    }
}
