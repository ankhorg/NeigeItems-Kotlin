package pers.neige.neigeitems.action.impl;

import org.jetbrains.annotations.NotNull;
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
    @NotNull
    private final String scriptString;
    @NotNull
    private final CompiledScript script;

    public JsAction(
            @NotNull BaseActionManager manager,
            @NotNull String scriptString
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
    public @NotNull ActionType getType() {
        return ActionType.JS;
    }

    /**
     * 将基础类型动作的执行逻辑放入 BaseActionManager 是为了给其他插件覆写的机会
     */
    @Override
    @NotNull
    protected CompletableFuture<ActionResult> eval(
            @NotNull BaseActionManager manager,
            @NotNull ActionContext context
    ) {
        return manager.runAction(this, context);
    }

    public @NotNull String getScriptString() {
        return scriptString;
    }

    public @NotNull CompiledScript getScript() {
        return script;
    }
}
