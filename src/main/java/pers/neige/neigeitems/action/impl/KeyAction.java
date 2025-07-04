package pers.neige.neigeitems.action.impl;

import lombok.NonNull;
import lombok.val;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.lazy.ThreadSafeLazyBoolean;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class KeyAction extends Action {
    private final @NonNull String globalId;
    private final @NonNull Evaluator<String> key;
    private final @NonNull Action defaultAction;
    private final @NonNull Action matchAction;
    private final @NonNull Map<String, Action> actions = new HashMap<>();

    public KeyAction(
            @NonNull BaseActionManager manager,
            @NonNull ConfigReader config
    ) {
        super(manager);
        this.globalId = config.getString("global-id", "key");
        this.key = Evaluator.createStringEvaluator(manager, config.getString("key"));
        this.defaultAction = manager.compile(config.get("default-action"));
        this.matchAction = manager.compile(config.get("match-action"));
        val actionsConfig = config.getConfig("actions");
        if (actionsConfig != null) {
            for (val key : actionsConfig.keySet()) {
                this.actions.put(key, manager.compile(actionsConfig.get(key)));
            }
        }
        checkAsyncSafe();
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

    @Override
    public @NonNull ActionType getType() {
        return ActionType.KEY;
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

    public @NonNull String getGlobalId() {
        return globalId;
    }

    public @NonNull Evaluator<String> getKey() {
        return key;
    }

    public @NonNull Action getDefaultAction() {
        return defaultAction;
    }

    public @NonNull Action getMatchAction() {
        return matchAction;
    }

    public @NonNull Map<String, Action> getActions() {
        return actions;
    }
}
