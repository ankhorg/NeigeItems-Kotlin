package pers.neige.neigeitems.action;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.SchedulerUtils;

public abstract class Action {
    public static void eval(
            Action sync,
            Action async,
            BaseActionManager manager,
            ActionContext context
    ) {
        if (Bukkit.isPrimaryThread()) {
            sync.eval(manager, context);
            SchedulerUtils.async(manager.getPlugin(), () -> async.eval(manager, context));
        } else {
            SchedulerUtils.sync(manager.getPlugin(), () -> sync.eval(manager, context));
            async.eval(manager, context);
        }
    }

    @NotNull
    public abstract ActionType getType();

    @NotNull
    public ActionResult eval(
            @NotNull BaseActionManager manager,
            @NotNull ActionContext context
    ) {
        return manager.runAction(this, context);
    }
}
