package pers.neige.neigeitems.action;

import lombok.NonNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.manager.BaseActionManager;

import javax.script.Compilable;
import javax.script.ScriptException;

public class Condition {
    private final @NonNull BaseActionManager manager;
    private final @Nullable ScriptWithSource condition;

    public Condition(@NonNull BaseActionManager manager, @Nullable String conditionString) {
        this.manager = manager;
        try {
            this.condition = ScriptWithSource.compile((Compilable) manager.getEngine(), conditionString);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public @NonNull BaseActionManager getManager() {
        return manager;
    }

    public @Nullable ScriptWithSource getCondition() {
        return condition;
    }

    public @NonNull ActionResult check(@NonNull ActionContext context) {
        return manager.parseCondition(condition, context);
    }

    public boolean easyCheck(@NonNull ActionContext context) {
        return manager.parseCondition(condition, context).getType() == ResultType.SUCCESS;
    }
}
