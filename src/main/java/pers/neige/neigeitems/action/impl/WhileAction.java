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

public class WhileAction extends Action {
    @Nullable
    private final String condition;
    @NotNull
    private final Action actions;
    @NotNull
    private final Action async;
    @NotNull
    private final Action sync;
    @NotNull
    private final Action _finally;

    public WhileAction(BaseActionManager manager, ConfigurationSection action) {
        if (action.contains("while")) {
            condition = action.getString("while");
        } else {
            condition = null;
        }
        actions = manager.compile(action.get("actions"));
        async = manager.compile(action.get("async"));
        sync = manager.compile(action.get("sync"));
        _finally = manager.compile(action.get("finally"));
    }

    public WhileAction(BaseActionManager manager, Map<?, ?> action) {
        if (action.containsKey("while")) {
            Object value = action.get("while");
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
        _finally = manager.compile(action.get("finally"));
    }

    @Override
    public @NotNull ActionType getType() {
        return ActionType.WHILE;
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
    public Action getFinally() {
        return _finally;
    }
}
