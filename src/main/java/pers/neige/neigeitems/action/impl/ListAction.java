package pers.neige.neigeitems.action.impl;

import lombok.NonNull;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ListAction extends Action {
    private final @NonNull List<Action> actions;

    public ListAction(
            @NonNull BaseActionManager manager,
            @NonNull List<?> action
    ) {
        super(manager);
        actions = new ArrayList<>();
        for (Object it : action) {
            actions.add(manager.compile(it));
        }
        checkAsyncSafe();
    }

    private ListAction(
            @NonNull ListAction action,
            int fromIndex,
            int toIndex
    ) {
        super(action.manager);
        actions = action.actions.subList(fromIndex, toIndex);
        checkAsyncSafe();
    }

    private void checkAsyncSafe() {
        for (Action action : actions) {
            if (action.isAsyncSafe()) return;
        }
        this.asyncSafe = false;
    }

    @Override
    public @NonNull ActionType getType() {
        return ActionType.LIST;
    }

    /**
     * 将基础类型动作的执行逻辑放入 BaseActionManager 是为了给其他插件覆写的机会
     */
    @Override
    public @NonNull CompletableFuture<ActionResult> eval(
            @NonNull BaseActionManager manager,
            @NonNull ActionContext context
    ) {
        return manager.runAction(this, context);
    }

    /**
     * 将基础类型动作的执行逻辑放入 BaseActionManager 是为了给其他插件覆写的机会
     *
     * @param fromIndex 从这个索引对应的位置开始执行
     */
    protected @NonNull CompletableFuture<ActionResult> eval(
            @NonNull BaseActionManager manager,
            @NonNull ActionContext context,
            int fromIndex
    ) {
        return manager.runAction(this, context, fromIndex);
    }

    public @NonNull List<Action> getActions() {
        return actions;
    }

    public @NonNull ListAction subList(int fromIndex, int toIndex) {
        return new ListAction(this, fromIndex, toIndex);
    }
}
