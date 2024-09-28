package pers.neige.neigeitems.action.impl;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ListAction extends Action {
    @NotNull
    private final List<Action> actions;

    public ListAction(
            @NotNull BaseActionManager manager,
            @NotNull List<?> action
    ) {
        actions = new ArrayList<>();
        for (Object it : action) {
            actions.add(manager.compile(it));
        }
    }

    private ListAction(
            @NotNull ListAction action,
            int fromIndex,
            int toIndex
    ) {
        actions = action.actions.subList(fromIndex, toIndex);
    }

    @Override
    public @NotNull ActionType getType() {
        return ActionType.LIST;
    }

    /**
     * 将基础类型动作的执行逻辑放入 BaseActionManager 是为了给其他插件覆写的机会
     */
    @Override
    @NotNull
    public CompletableFuture<ActionResult> eval(
            @NotNull BaseActionManager manager,
            @NotNull ActionContext context
    ) {
        return manager.runAction(this, context);
    }

    /**
     * 将基础类型动作的执行逻辑放入 BaseActionManager 是为了给其他插件覆写的机会
     *
     * @param fromIndex 从这个索引对应的位置开始执行
     */
    @NotNull
    public CompletableFuture<ActionResult> eval(
            @NotNull BaseActionManager manager,
            @NotNull ActionContext context,
            int fromIndex
    ) {
        return manager.runAction(this, context, fromIndex);
    }

    @NotNull
    public List<Action> getActions() {
        return actions;
    }

    @NotNull
    public ListAction subList(int fromIndex, int toIndex) {
        return new ListAction(this, fromIndex, toIndex);
    }
}
