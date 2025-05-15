package pers.neige.neigeitems.action.impl;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.action.evaluator.Evaluator;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ContainsAction extends Action {
    @NotNull
    private final String globalId;
    @NotNull
    private final Evaluator<String> key;
    @NotNull
    private final Action defaultAction;
    @NotNull
    private final Action containsAction;
    @NotNull
    private final Set<String> elements;

    public ContainsAction(
            @NotNull BaseActionManager manager,
            @NotNull ConfigReader config
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
        if (this.defaultAction.isAsyncSafe()) return;
        if (this.containsAction.isAsyncSafe()) return;
        this.asyncSafe = false;
    }

    @Override
    public @NotNull ActionType getType() {
        return ActionType.CONTAINS;
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
    public Action getContainsAction() {
        return containsAction;
    }

    public @NotNull Set<String> getElements() {
        return elements;
    }
}
