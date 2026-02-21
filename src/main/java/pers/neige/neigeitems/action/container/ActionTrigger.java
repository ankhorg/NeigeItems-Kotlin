package pers.neige.neigeitems.action.container;

import lombok.NonNull;
import org.bukkit.configuration.ConfigurationSection;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.SchedulerUtils;

import java.util.concurrent.CompletableFuture;

public class ActionTrigger {
    private final @NonNull BaseActionManager actionManager;
    private final @NonNull String type;
    private @NonNull Action actions;
    private @NonNull Action async;
    private @NonNull Action sync;

    public ActionTrigger(
        @NonNull BaseActionManager actionManager,
        @NonNull String type,
        @NonNull ConfigurationSection config
    ) {
        this.actionManager = actionManager;
        this.type = type;
        this.actions = actionManager.compile(config.get("actions"));
        this.async = actionManager.compile(config.get("async"));
        this.sync = actionManager.compile(config.get("sync"));
    }

    public ActionTrigger(
        @NonNull BaseActionManager actionManager,
        @NonNull String type,
        @NonNull Action actions
    ) {
        this.actionManager = actionManager;
        this.type = type;
        this.actions = actions;
        this.async = actionManager.NULL_ACTION;
        this.sync = actionManager.NULL_ACTION;
    }

    public @NonNull BaseActionManager getActionManager() {
        return actionManager;
    }

    public @NonNull String getType() {
        return type;
    }

    public @NonNull Action getActions() {
        return actions;
    }

    public void setActions(@NonNull Action actions) {
        this.actions = actions;
    }

    public @NonNull Action getAsync() {
        return async;
    }

    public void setAsync(@NonNull Action async) {
        this.async = async;
    }

    public @NonNull Action getSync() {
        return sync;
    }

    public void setSync(@NonNull Action sync) {
        this.sync = sync;
    }

    public @NonNull CompletableFuture<ActionResult> run(
        @NonNull ActionContext context
    ) {
        async(context);
        sync(context);
        return actions.eval(context);
    }

    public void async(
        @NonNull ActionContext context
    ) {
        SchedulerUtils.async(actionManager.getPlugin(), () -> {
            async.eval(context.clone());
        });
    }

    public void sync(
        @NonNull ActionContext context
    ) {
        SchedulerUtils.sync(actionManager.getPlugin(), () -> {
            sync.eval(context.clone());
        });
    }
}