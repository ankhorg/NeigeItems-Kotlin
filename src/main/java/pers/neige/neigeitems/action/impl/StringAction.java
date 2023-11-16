package pers.neige.neigeitems.action.impl;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.StringUtils;

import java.util.List;
import java.util.Locale;

public class StringAction extends Action {
    @NotNull
    private final String action;
    @NotNull
    private final String key;
    @NotNull
    private final String content;

    public StringAction(
            @NotNull String action
    ) {
        this.action = action;
        List<String> info = StringUtils.splitKt(action, ": ", 2);
        key = info.get(0).toLowerCase(Locale.ROOT);
        content = info.size() > 1 ? info.get(1) : "";
    }

    @Override
    public @NotNull ActionType getType() {
        return ActionType.STRING;
    }

    @Override
    @NotNull
    public ActionResult run(
            @NotNull BaseActionManager manager,
            @NotNull ActionContext context
    ) {
        return manager.runAction(this, context);
    }

    @NotNull
    public String getAction() {
        return action;
    }

    @NotNull
    public String getKey() {
        return key;
    }

    @NotNull
    public String getContent() {
        return content;
    }
}
