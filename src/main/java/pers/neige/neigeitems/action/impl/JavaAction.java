package pers.neige.neigeitems.action.impl;

import lombok.NonNull;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public class JavaAction extends Action {
    private final @NonNull BiFunction<BaseActionManager, ActionContext, CompletableFuture<ActionResult>> function;

    public JavaAction(
            @NonNull BaseActionManager manager,
            @NonNull BiFunction<BaseActionManager, ActionContext, CompletableFuture<ActionResult>> function
    ) {
        super(manager);
        this.function = function;
    }

    public JavaAction(
            @NonNull BaseActionManager manager,
            boolean asyncSafe,
            @NonNull BiFunction<BaseActionManager, ActionContext, CompletableFuture<ActionResult>> function
    ) {
        super(manager);
        this.asyncSafe = asyncSafe;
        this.function = function;
    }

    @Override
    public @NonNull ActionType getType() {
        return ActionType.JAVA;
    }

    @Override
    protected @NonNull CompletableFuture<ActionResult> eval(
            @NonNull BaseActionManager manager,
            @NonNull ActionContext context
    ) {
        return function.apply(manager, context);
    }

    public @NonNull BiFunction<BaseActionManager, ActionContext, CompletableFuture<ActionResult>> getFunction() {
        return function;
    }
}
