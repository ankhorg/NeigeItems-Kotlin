package pers.neige.neigeitems.action.impl;

import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.Map;

public class ConditionAction extends Action {
    @Nullable
    private final String condition;
    @NotNull
    private final Action actions;
    @NotNull
    private final Action async;
    @NotNull
    private final Action sync;
    @NotNull
    private final Action deny;

    public ConditionAction(BaseActionManager manager, ConfigurationSection action) {
        if (action.contains("condition")) {
            condition = action.getString("condition");
        } else {
            condition = null;
        }
        actions = manager.compile(action.get("actions"));
        async = manager.compile(action.get("async"));
        sync = manager.compile(action.get("sync"));
        deny = manager.compile(action.get("deny"));
    }

    public ConditionAction(BaseActionManager manager, Map<?, ?> action) {
        if (action.containsKey("condition")) {
            Object value = action.get("condition");
            if (value instanceof String) {
                condition = (String) value;
            } else {
                condition = null;
            }
        } else {
            condition = null;
        }
        actions = manager.compile(action.get("actions"));
        async = manager.compile(action.get("async"));
        sync = manager.compile(action.get("sync"));
        deny = manager.compile(action.get("deny"));
    }

    @Override
    public @NotNull ActionType getType() {
        return ActionType.CONDITION;
    }

    /**
     * 将基础类型动作的执行逻辑放入 BaseActionManager 是为了给其他插件覆写的机会
     */
    @Override
    @NotNull
    public ActionResult eval(
            @NotNull BaseActionManager manager,
            @NotNull ActionContext context
    ) {
        return manager.runAction(this, context);
    }

    @Nullable
    public String getCondition() {
        return condition;
    }

    @NotNull
    public Action getActions() {
        return actions;
    }

    @NotNull
    public Action getAsync() {
        return async;
    }

    @NotNull
    public Action getSync() {
        return sync;
    }

    @NotNull
    public Action getDeny() {
        return deny;
    }
}
