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
    public ActionResult run(
            @NotNull BaseActionManager manager,
            @NotNull ActionContext context
    ) {
        return manager.runAction(this, context);
    }

    @NotNull
    public static Action parse(
            @Nullable Object action
    ) {
        if (action instanceof String) {
            return new StringAction((String) action);
        } else if (action instanceof List<?>) {
            return new ListAction((List<?>) action);
        } else if (action instanceof Map<?, ?>) {
            if (((Map<?, ?>) action).containsKey("condition")) {
                return new ConditionAction((Map<?, ?>) action);
            } else if (((Map<?, ?>) action).containsKey("while")) {
                return new WhileAction((Map<?, ?>) action);
            }
        } else if (action instanceof ConfigurationSection) {
            if (((ConfigurationSection) action).contains("condition")) {
                return new ConditionAction((ConfigurationSection) action);
            } else if (((ConfigurationSection) action).contains("while")) {
                return new WhileAction((ConfigurationSection) action);
            }
        }
        return NullAction.INSTANCE;
    }

    public static void run(
            Action sync,
            Action async,
            BaseActionManager manager,
            ActionContext context
    ) {
        if (Bukkit.isPrimaryThread()) {
            sync.run(manager, context);
            SchedulerUtils.async(manager.getPlugin(), () -> async.run(manager, context));
        } else {
            SchedulerUtils.sync(manager.getPlugin(), () -> sync.run(manager, context));
            async.run(manager, context);
        }
    }
}
