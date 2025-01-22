package pers.neige.neigeitems.action;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.manager.BaseActionManager;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptException;

public class Condition {
    @NotNull
    private final BaseActionManager manager;
    @Nullable
    private final String conditionString;
    @Nullable
    private final CompiledScript condition;

    public Condition(@NotNull BaseActionManager manager, @Nullable String conditionString) {
        this.manager = manager;
        this.conditionString = conditionString;
        if (this.conditionString != null) {
            try {
                this.condition = ((Compilable) manager.getEngine()).compile(conditionString);
            } catch (ScriptException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.condition = null;
        }
    }

    @NotNull
    public ActionResult check(@NotNull ActionContext context) {
        return manager.parseCondition(conditionString, condition, context);
    }

    public boolean easyCheck(@NotNull ActionContext context) {
        return manager.parseCondition(conditionString, condition, context).getType() == ResultType.SUCCESS;
    }
}
