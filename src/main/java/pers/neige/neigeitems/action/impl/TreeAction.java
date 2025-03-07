package pers.neige.neigeitems.action.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.Locale;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

public abstract class TreeAction<T extends Comparable<?>> extends Action {
    @NotNull
    protected final Class<T> keyType;
    @NotNull
    private final TreeActionType actionType;
    @NotNull
    private final String globalId;
    @NotNull
    private final Action defaultAction;
    @NotNull
    private final TreeMap<T, Action> actions = new TreeMap<>();

    public TreeAction(
            @NotNull BaseActionManager manager,
            @NotNull ConfigReader config,
            @NotNull Class<T> keyType
    ) {
        super(manager);
        this.keyType = keyType;
        Object rawActionType = config.get("action-type");
        TreeActionType actionType;
        try {
            if (rawActionType instanceof String) {
                actionType = TreeActionType.valueOf(((String) rawActionType).toUpperCase(Locale.ROOT));
            } else if (rawActionType instanceof Integer) {
                actionType = TreeActionType.values()[(int) rawActionType];
            } else {
                actionType = TreeActionType.LOWER;
            }
        } catch (Throwable throwable) {
            manager.getPlugin().getLogger().warning(rawActionType + " is not a valid tree action type.");
            actionType = TreeActionType.LOWER;
        }
        this.actionType = actionType;
        this.globalId = config.getString("global-id", "key");
        this.defaultAction = manager.compile(config.get("default-action"));
        ConfigReader actionsConfig = config.getConfig("actions");
        if (actionsConfig != null) {
            for (String stringKey : actionsConfig.keySet()) {
                T key = cast(stringKey);
                if (key == null) {
                    manager.getPlugin().getLogger().warning(stringKey + " is not a valid tree action key.");
                    continue;
                }
                this.actions.put(key, manager.compile(actionsConfig.get(stringKey)));
            }
        }
        checkAsyncSafe();
    }

    @Override
    public @NotNull ActionType getType() {
        return ActionType.TREE;
    }

    public @Nullable T cast(@NotNull Object result) {
        return keyType.isInstance(result) ? keyType.cast(result) : null;
    }

    private void checkAsyncSafe() {
        for (Action action : this.actions.values()) {
            if (action.isAsyncSafe()) return;
        }
        if (this.defaultAction.isAsyncSafe()) return;
        this.asyncSafe = false;
    }

    @NotNull
    public Class<T> getKeyType() {
        return keyType;
    }

    @NotNull
    public TreeActionType getActionType() {
        return actionType;
    }

    @NotNull
    public String getGlobalId() {
        return globalId;
    }

    @NotNull
    public abstract Evaluator<T> getKey();

    @NotNull
    public Action getDefaultAction() {
        return defaultAction;
    }

    @NotNull
    public TreeMap<T, Action> getActions() {
        return actions;
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

    public enum TreeActionType {
        LOWER,
        FLOOR,
        HIGHER,
        CEILING
    }
}
