package pers.neige.neigeitems.action.impl;

import lombok.NonNull;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.concurrent.CompletableFuture;

public class LabelAction extends Action {
    private final @NonNull String label;
    private final @NonNull Action actions;

    public LabelAction(
            @NonNull BaseActionManager manager,
            @NonNull ConfigReader action
    ) {
        super(manager);
        if (action.containsKey("label")) {
            label = action.getString("label", "label");
        } else {
            label = "label";
        }
        actions = manager.compile(action.get("actions"));
        this.asyncSafe = actions.isAsyncSafe();
    }

    @Override
    public @NonNull ActionType getType() {
        return ActionType.LABEL;
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

    public @NonNull String getLabel() {
        return label;
    }

    public @NonNull Action getActions() {
        return actions;
    }
}
