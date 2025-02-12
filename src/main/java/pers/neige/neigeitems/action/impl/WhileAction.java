package pers.neige.neigeitems.action.impl;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.action.*;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.concurrent.CompletableFuture;

public class WhileAction extends Action {
    @NotNull
    private final Condition condition;
    @NotNull
    private final Action actions;
    @NotNull
    private final Action _finally;

    public WhileAction(
            @NotNull BaseActionManager manager,
            @NotNull ConfigReader action
    ) {
        super(manager);
        condition = new Condition(manager, action.getString("while"));
        actions = manager.compile(action.get("actions"));
        _finally = manager.compile(action.get("finally"));
        checkAsyncSafe();
    }

    private void checkAsyncSafe() {
        if (!this.actions.isAsyncSafe() && !this._finally.isAsyncSafe()) {
            this.asyncSafe = false;
        }
    }

    @Override
    public @NotNull ActionType getType() {
        return ActionType.WHILE;
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

    @NotNull
    public Condition getCondition() {
        return condition;
    }

    @NotNull
    public Action getActions() {
        return actions;
    }

    @NotNull
    public Action getFinally() {
        return _finally;
    }
}
