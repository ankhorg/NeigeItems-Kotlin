package pers.neige.neigeitems.action.impl;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.ArrayList;
import java.util.List;

public class ListAction extends Action {
    @NotNull
    private final List<Action> actions;

    public ListAction(BaseActionManager manager, List<?> action) {
        actions = new ArrayList<>();
        for (Object it : action) {
            actions.add(manager.compile(it));
        }
    }

    private ListAction(ListAction action, int fromIndex, int toIndex) {
        actions = action.actions.subList(fromIndex, toIndex);
    }

    @Override
    public @NotNull ActionType getType() {
        return ActionType.LIST;
    }

    @Override
    @NotNull
    public ActionResult eval(
            @NotNull BaseActionManager manager,
            @NotNull ActionContext context
    ) {
        return manager.runAction(this, context);
    }

    @NotNull
    public List<Action> getActions() {
        return actions;
    }

    @NotNull
    public ListAction subList(int fromIndex, int toIndex) {
        return new ListAction(this, fromIndex, toIndex);
    }
}
