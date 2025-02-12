package pers.neige.neigeitems.action.impl;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.action.*;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.concurrent.CompletableFuture;

public class ConditionAction extends Action {
    @NotNull
    private final Condition condition;
    @NotNull
    private final Action actions;
    @NotNull
    private final Action async;
    @NotNull
    private final Action sync;
    @NotNull
    private final Action deny;

    public ConditionAction(
            @NotNull BaseActionManager manager,
            @NotNull ConfigReader action
    ) {
        super(manager);
        condition = new Condition(manager, action.getString("condition"));
        actions = manager.compile(action.get("actions"));
        async = manager.compile(action.get("async"));
        sync = manager.compile(action.get("sync"));
        deny = manager.compile(action.get("deny"));
        checkAsyncSafe();
    }

    private void checkAsyncSafe() {
        if (!this.actions.isAsyncSafe() && !this.deny.isAsyncSafe()) {
            this.asyncSafe = false;
        }
    }

    @Override
    public @NotNull ActionType getType() {
        return ActionType.CONDITION;
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
    public Action getAsync() {
        return async;
    }

    @NotNull
    public Action getSync() {
        return sync;
    }

    @NotNull
    public Action getDeny() {
        return deny;
    }
}
