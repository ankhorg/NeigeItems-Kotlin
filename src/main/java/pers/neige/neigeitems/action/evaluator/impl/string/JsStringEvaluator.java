package pers.neige.neigeitems.action.evaluator.impl.string;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.impl.JsEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

public class JsStringEvaluator extends JsEvaluator<String> {
    public JsStringEvaluator(@NotNull BaseActionManager manager, @Nullable String script) {
        super(manager, String.class, script);
    }

    @Override
    public @Nullable String cast(@NotNull Object result) {
        return result.toString();
    }
}
