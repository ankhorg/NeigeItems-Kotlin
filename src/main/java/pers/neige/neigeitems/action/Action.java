package pers.neige.neigeitems.action;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.concurrent.CompletableFuture;

public abstract class Action {
    @NotNull
    public ActionType getType() {
        return ActionType.UNKNOWN;
    }

    @NotNull
    public abstract CompletableFuture<ActionResult> eval(
            @NotNull BaseActionManager manager,
            @NotNull ActionContext context
    );
}
