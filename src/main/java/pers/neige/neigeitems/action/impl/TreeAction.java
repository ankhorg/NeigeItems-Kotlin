package pers.neige.neigeitems.action.impl;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.lazy.ThreadSafeLazyBoolean;

import java.util.Locale;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

public abstract class TreeAction<T extends Comparable<?>> extends Action {
    protected final @NonNull Class<T> keyType;
    private final @NonNull TreeActionType actionType;
    private final @NonNull String globalId;
    private final @NonNull Action defaultAction;
    private final @NonNull Action matchAction;
    private final @NonNull TreeMap<T, Action> actions = new TreeMap<>();

    public TreeAction(
            @NonNull BaseActionManager manager,
            @NonNull ConfigReader config,
            @NonNull Class<T> keyType
    ) {
        super(manager);
        this.keyType = keyType;
        val rawActionType = config.get("action-type");
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
        this.matchAction = manager.compile(config.get("match-action"));
        val actionsConfig = config.getConfig("actions");
        if (actionsConfig != null) {
            for (val stringKey : actionsConfig.keySet()) {
                val key = cast(stringKey);
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
    public @NonNull ActionType getType() {
        return ActionType.TREE;
    }

    public @Nullable T cast(@NonNull Object result) {
        return keyType.isInstance(result) ? keyType.cast(result) : null;
    }

    private void checkAsyncSafe() {
        this.canRunInOtherThread = new ThreadSafeLazyBoolean(() -> {
            for (val action : this.actions.values()) {
                if (action.canRunInOtherThread()) return true;
            }
            if (this.defaultAction.canRunInOtherThread()) return true;
            return this.matchAction.canRunInOtherThread();
        });
    }

    public @NonNull Class<T> getKeyType() {
        return keyType;
    }

    public @NonNull TreeActionType getActionType() {
        return actionType;
    }

    public @NonNull String getGlobalId() {
        return globalId;
    }

    public abstract @NonNull Evaluator<T> getKey();

    public @NonNull Action getDefaultAction() {
        return defaultAction;
    }

    public @NonNull Action getMatchAction() {
        return matchAction;
    }

    public @NonNull TreeMap<T, Action> getActions() {
        return actions;
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

    public enum TreeActionType {
        LOWER,
        FLOOR,
        HIGHER,
        CEILING
    }
}
