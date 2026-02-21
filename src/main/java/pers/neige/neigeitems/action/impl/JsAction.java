package pers.neige.neigeitems.action.impl;

import lombok.NonNull;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.manager.BaseActionManager;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptException;
import java.util.concurrent.CompletableFuture;

public class JsAction extends Action {
    private final @NonNull String scriptString;
    private final @NonNull CompiledScript script;

    public JsAction(
        @NonNull BaseActionManager manager,
        @NonNull String scriptString
    ) {
        super(manager);
        this.scriptString = scriptString;
        try {
            this.script = ((Compilable) manager.getEngine()).compile(scriptString);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NonNull ActionType getType() {
        return ActionType.JS;
    }

    /**
     * 将基础类型动作的执行逻辑放入 BaseActionManager 是为了给其他插件覆写的机会
     */
    @Override
    protected @NonNull CompletableFuture<ActionResult> eval(
        @NonNull BaseActionManager manager,
        @NonNull ActionContext context
    ) {
        return manager.runAction(this, context);
    }

    public @NonNull String getScriptString() {
        return scriptString;
    }

    public @NonNull CompiledScript getScript() {
        return script;
    }
}
