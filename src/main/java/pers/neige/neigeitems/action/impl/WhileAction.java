package pers.neige.neigeitems.action.impl;

import lombok.NonNull;
import pers.neige.neigeitems.action.*;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.lazy.ThreadSafeLazyBoolean;

import java.util.concurrent.CompletableFuture;

public class WhileAction extends Action {
    private final @NonNull Condition condition;
    private final @NonNull Action actions;
    private final @NonNull Action _finally;

    public WhileAction(
            @NonNull BaseActionManager manager,
            @NonNull ConfigReader action
    ) {
        super(manager);
        condition = new Condition(manager, action.getString("while"));
        actions = manager.compile(action.get("actions"));
        _finally = manager.compile(action.get("finally"));
        checkAsyncSafe();
    }

    private void checkAsyncSafe() {
        this.canRunInOtherThread = new ThreadSafeLazyBoolean(() -> this.actions.canRunInOtherThread() || this._finally.canRunInOtherThread());
    }

    @Override
    public @NonNull ActionType getType() {
        return ActionType.WHILE;
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

    public @NonNull Action getFinally() {
        return _finally;
    }
}
