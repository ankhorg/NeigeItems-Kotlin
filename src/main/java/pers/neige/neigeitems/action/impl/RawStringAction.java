package pers.neige.neigeitems.action.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.action.handler.SyncActionHandler;
import pers.neige.neigeitems.action.result.Results;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public class RawStringAction extends Action {
    @NotNull
    private final String action;
    @NotNull
    private final String key;
    @NotNull
    private final String content;
    @Nullable
    private BiFunction<ActionContext, String, CompletableFuture<ActionResult>> handler;

    public RawStringAction(
            @NotNull BaseActionManager manager,
            @NotNull String action
    ) {
        super(manager);
        this.action = action;
        String[] info = action.split(": ", 2);
        key = info[0].toLowerCase(Locale.ROOT);
        content = info.length > 1 ? info[1] : "";
        this.handler = manager.getActions().get(this.key);
        checkAsyncSafe();
    }

    public RawStringAction(
            @NotNull BaseActionManager manager,
            @NotNull String action,
            @NotNull String key,
            @NotNull String content
    ) {
        super(manager);
        this.action = action;
        this.key = key;
        this.content = content;
        this.handler = manager.getActions().get(this.key);
        checkAsyncSafe();
    }

    private void checkAsyncSafe() {
        if (this.handler != null && this.handler instanceof SyncActionHandler) {
            this.asyncSafe = false;
        }
    }

    @Override
    public @NotNull ActionType getType() {
        return ActionType.RAW_STRING;
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

    @Override
    public @NotNull CompletableFuture<ActionResult> evalAsyncSafe(@NotNull BaseActionManager manager, @NotNull ActionContext context) {
        if (handler == null) {
            this.handler = manager.getActions().get(this.key);
            checkAsyncSafe();
        }
        return super.evalAsyncSafe(manager, context);
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

    @Nullable
    public BiFunction<ActionContext, String, CompletableFuture<ActionResult>> getHandler() {
        return handler;
    }
}
