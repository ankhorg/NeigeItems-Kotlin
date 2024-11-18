package pers.neige.neigeitems.action.container;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.impl.NullAction;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.SchedulerUtils;

import java.util.concurrent.CompletableFuture;

public class ActionTrigger {
    @NotNull
    private final BaseActionManager actionManager;
    @NotNull
    private final String type;
    @NotNull
    private final Action actions;
    @NotNull
    private final Action async;
    @NotNull
    private final Action sync;

    public ActionTrigger(
            @NotNull BaseActionManager actionManager,
            @NotNull String type,
            @NotNull ConfigurationSection config
    ) {
        this.actionManager = actionManager;
        this.type = type;
        this.actions = actionManager.compile(config.get("actions"));
        this.async = actionManager.compile(config.get("async"));
        this.sync = actionManager.compile(config.get("sync"));
    }

    public ActionTrigger(
            @NotNull BaseActionManager actionManager,
            @NotNull String type,
            @NotNull Action actions
    ) {
        this.actionManager = actionManager;
        this.type = type;
        this.actions = actions;
        this.async = NullAction.INSTANCE;
        this.sync = NullAction.INSTANCE;
    }

    @NotNull
    public String getType() {
        return type;
    }

    @NotNull
    public CompletableFuture<ActionResult> run(
            @NotNull ActionContext context
    ) {
        async(context);
        sync(context);
        return actionManager.runActionWithResult(actions, context);
    }

    public void async(
            @NotNull ActionContext context
    ) {
        SchedulerUtils.async(actionManager.getPlugin(), () -> {
            try {
                actionManager.runAction(async, context.clone());
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void sync(
            @NotNull ActionContext context
    ) {
        SchedulerUtils.sync(actionManager.getPlugin(), () -> {
            try {
                actionManager.runAction(sync, context.clone());
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}