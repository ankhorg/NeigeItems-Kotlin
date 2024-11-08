package pers.neige.neigeitems.action.impl;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.action.result.Results;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.StringUtils;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

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
        String[] info = StringUtils.splitOnce(action, ": ");
        key = info[0].toLowerCase(Locale.ROOT);
        content = info.length > 1 ? info[1] : "";
    }

    public StringAction(
            @NotNull String action,
            @NotNull String key,
            @NotNull String content
    ) {
        this.action = action;
        this.key = key;
        this.content = content;
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
    public CompletableFuture<ActionResult> eval(
            @NotNull BaseActionManager manager,
            @NotNull ActionContext context
    ) {
        try {
            return manager.runAction(this, context);
        } catch (Throwable throwable) {
            manager.getPlugin().getLogger().warning("动作执行异常, 动作原始内容如下:");
            for (String actionLine : action.split("\n")) {
                manager.getPlugin().getLogger().warning(actionLine);
            }
            throwable.printStackTrace();
            return CompletableFuture.completedFuture(Results.STOP);
        }
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
