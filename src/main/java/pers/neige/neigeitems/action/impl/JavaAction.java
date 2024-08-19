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
            @NotNull BiFunction<BaseActionManager, ActionContext, CompletableFuture<ActionResult>> function
    ) {
        this.function = function;
    }

    @Override
    public @NotNull ActionType getType() {
        return ActionType.STRING;
    }

    @Override
    @NotNull
    public CompletableFuture<ActionResult> eval(
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
