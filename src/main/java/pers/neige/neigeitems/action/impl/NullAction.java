package pers.neige.neigeitems.action.impl;

import lombok.NonNull;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.action.result.Results;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.concurrent.CompletableFuture;

public class NullAction extends Action {
    public NullAction(@NonNull BaseActionManager manager) {
        super(manager);
    }

    @Override
    public @NonNull ActionType getType() {
        return ActionType.NULL;
    }

    /**
     * 将基础类型动作的执行逻辑放入 BaseActionManager 是为了给其他插件覆写的机会
     */
    @Override
    protected @NonNull CompletableFuture<ActionResult> eval(
        @NonNull BaseActionManager manager,
        @NonNull ActionContext context
    ) {
        return CompletableFuture.completedFuture(Results.SUCCESS);
    }
}
