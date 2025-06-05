package pers.neige.neigeitems.action.impl;

import lombok.NonNull;
import pers.neige.neigeitems.action.*;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.concurrent.CompletableFuture;

public class ConditionAction extends Action {
    private final @NonNull Condition condition;
    private final @NonNull Action actions;
    private final @NonNull Action async;
    private final @NonNull Action sync;
    private final @NonNull Action deny;

    public ConditionAction(
            @NonNull BaseActionManager manager,
            @NonNull ConfigReader action
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
    public @NonNull ActionType getType() {
        return ActionType.CONDITION;
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

    public @NonNull Condition getCondition() {
        return condition;
    }

    public @NonNull Action getActions() {
        return actions;
    }

    public @NonNull Action getAsync() {
        return async;
    }

    public @NonNull Action getSync() {
        return sync;
    }

    public @NonNull Action getDeny() {
        return deny;
    }
}
