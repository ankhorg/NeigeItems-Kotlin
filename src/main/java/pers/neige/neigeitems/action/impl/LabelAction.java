package pers.neige.neigeitems.action.impl;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.concurrent.CompletableFuture;

public class LabelAction extends Action {
    @NotNull
    private final String label;
    @NotNull
    private final Action actions;

    public LabelAction(
            @NotNull BaseActionManager manager,
            @NotNull ConfigReader action
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
    public @NotNull ActionType getType() {
        return ActionType.LABEL;
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
    public String getLabel() {
        return label;
    }

    @NotNull
    public Action getActions() {
        return actions;
    }
}
