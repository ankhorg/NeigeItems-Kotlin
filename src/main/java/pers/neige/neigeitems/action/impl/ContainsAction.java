package pers.neige.neigeitems.action.impl;

import lombok.NonNull;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.lazy.ThreadSafeLazyBoolean;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ContainsAction extends Action {
    private final @NonNull String globalId;
    private final @NonNull Evaluator<String> key;
    private final @NonNull Action defaultAction;
    private final @NonNull Action containsAction;
    private final @NonNull Set<String> elements;

    public ContainsAction(
        @NonNull BaseActionManager manager,
        @NonNull ConfigReader config
    ) {
        super(manager);
        this.globalId = config.getString("global-id", "key");
        this.key = Evaluator.createStringEvaluator(manager, config.getString("key"));
        this.defaultAction = manager.compile(config.get("default-action"));
        this.containsAction = manager.compile(config.get("contains-action"));
        this.elements = new HashSet<>(config.getStringList("elements"));
        checkAsyncSafe();
    }

    private void checkAsyncSafe() {
        this.canRunInOtherThread = new ThreadSafeLazyBoolean(() -> {
            if (this.defaultAction.canRunInOtherThread()) return true;
            return this.containsAction.canRunInOtherThread();
        });
    }

    @Override
    public @NonNull ActionType getType() {
        return ActionType.CONTAINS;
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

    public @NonNull Action getContainsAction() {
        return containsAction;
    }

    public @NonNull Set<String> getElements() {
        return elements;
    }
}
