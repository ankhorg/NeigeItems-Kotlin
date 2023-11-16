package pers.neige.neigeitems.action.impl;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.action.result.Results;
import pers.neige.neigeitems.manager.BaseActionManager;

public class NullAction extends Action {
    public static Action INSTANCE = new NullAction();

    @Override
    public @NotNull ActionType getType() {
        return ActionType.NULL;
    }
    @Override
    @NotNull
    public ActionResult run(
            @NotNull BaseActionManager manager,
            @NotNull ActionContext context
    ) {
        return Results.SUCCESS;
    }
}
