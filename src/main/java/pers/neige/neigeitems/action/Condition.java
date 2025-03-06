package pers.neige.neigeitems.action;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.manager.BaseActionManager;

import javax.script.Compilable;
import javax.script.ScriptException;

public class Condition {
    @NotNull
    private final BaseActionManager manager;
    @Nullable
    private final ScriptWithSource condition;

    public Condition(@NotNull BaseActionManager manager, @Nullable String conditionString) {
        this.manager = manager;
        try {
            this.condition = ScriptWithSource.compile((Compilable) manager.getEngine(), conditionString);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    public @NotNull BaseActionManager getManager() {
        return manager;
    }

    public @Nullable ScriptWithSource getCondition() {
        return condition;
    }

    @NotNull
    public ActionResult check(@NotNull ActionContext context) {
        return manager.parseCondition(condition, context);
    }

    public boolean easyCheck(@NotNull ActionContext context) {
        return manager.parseCondition(condition, context).getType() == ResultType.SUCCESS;
    }
}
