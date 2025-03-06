package pers.neige.neigeitems.action.impl;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class KeyAction extends Action {
    @NotNull
    private final String globalId;
    @NotNull
    private final Evaluator<String> key;
    @NotNull
    private final Action defaultAction;
    @NotNull
    private final Map<String, Action> actions = new HashMap<>();

    public KeyAction(
            @NotNull BaseActionManager manager,
            @NotNull ConfigReader config
    ) {
        super(manager);
        this.globalId = config.getString("global-id", "key");
        this.key = Evaluator.createStringEvaluator(manager, config.getString("key", ""));
        this.defaultAction = manager.compile(config.get("default-action"));
        ConfigReader actionsConfig = config.getConfig("actions");
        if (actionsConfig != null) {
            for (String key : actionsConfig.keySet()) {
                this.actions.put(key, manager.compile(actionsConfig.get(key)));
            }
        }
        checkAsyncSafe();
    }

    private void checkAsyncSafe() {
        for (Action action : this.actions.values()) {
            if (action.isAsyncSafe()) return;
        }
        if (this.defaultAction.isAsyncSafe()) return;
        this.asyncSafe = false;
    }

    @Override
    public @NotNull ActionType getType() {
        return ActionType.KEY;
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

    @NotNull
    public String getGlobalId() {
        return globalId;
    }

    @NotNull
    public Evaluator<String> getKey() {
        return key;
    }

    @NotNull
    public Action getDefaultAction() {
        return defaultAction;
    }

    @NotNull
    public Map<String, Action> getActions() {
        return actions;
    }
}
