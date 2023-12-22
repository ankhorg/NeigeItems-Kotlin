package pers.neige.neigeitems.action.impl;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.manager.BaseActionManager;

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
        String[] info = action.split(": ", 2);
        key = info[0].toLowerCase(Locale.ROOT);
        content = info.length > 1 ? info[1] : "";
    }

    @Override
    public @NotNull ActionType getType() {
        return ActionType.STRING;
    }

    /**
     * 将基础类型动作的执行逻辑放入 BaseActionManager 是为了给其他插件覆写的机会
     */
    @Override
    @NotNull
    public ActionResult eval(
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
