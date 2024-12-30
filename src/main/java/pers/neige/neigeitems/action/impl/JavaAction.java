package pers.neige.neigeitems.action.impl;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public class JavaAction extends Action {
    @NotNull
    private final BiFunction<BaseActionManager, ActionContext, CompletableFuture<ActionResult>> function;

    public JavaAction(
            @NotNull BaseActionManager manager,
            @NotNull BiFunction<BaseActionManager, ActionContext, CompletableFuture<ActionResult>> function
    ) {
        super(manager);
        this.function = function;
    }

    public JavaAction(
            @NotNull BaseActionManager manager,
            boolean asyncSafe,
            @NotNull BiFunction<BaseActionManager, ActionContext, CompletableFuture<ActionResult>> function
    ) {
        super(manager);
        this.asyncSafe = asyncSafe;
        this.function = function;
    }

    @Override
    public @NotNull ActionType getType() {
        return ActionType.JAVA;
    }

    @Override
    @NotNull
    protected CompletableFuture<ActionResult> eval(
            @NotNull BaseActionManager manager,
            @NotNull ActionContext context
    ) {
        return function.apply(manager, context);
    }

    @NotNull
    public BiFunction<BaseActionManager, ActionContext, CompletableFuture<ActionResult>> getFunction() {
        return function;
    }
}
