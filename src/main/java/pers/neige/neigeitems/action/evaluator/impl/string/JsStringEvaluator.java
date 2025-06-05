package pers.neige.neigeitems.action.evaluator.impl.string;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.evaluator.impl.JsEvaluator;
import pers.neige.neigeitems.manager.BaseActionManager;

public class JsStringEvaluator extends JsEvaluator<String> {
    public JsStringEvaluator(@NonNull BaseActionManager manager, @Nullable String script) {
        super(manager, String.class, script);
    }

    @Override
    public @Nullable String cast(@NonNull Object result) {
        return result.toString();
    }
}
