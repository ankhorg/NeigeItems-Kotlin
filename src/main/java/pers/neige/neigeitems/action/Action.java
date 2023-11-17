package pers.neige.neigeitems.action;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.impl.*;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.SchedulerUtils;

import java.util.List;
import java.util.Map;

public abstract class Action {
    @NotNull
    public abstract ActionType getType();

    @NotNull
    public ActionResult eval(
            @NotNull BaseActionManager manager,
            @NotNull ActionContext context
    ) {
        return manager.runAction(this, context);
    }

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
}
