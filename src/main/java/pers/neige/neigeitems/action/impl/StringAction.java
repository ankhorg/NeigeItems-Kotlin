package pers.neige.neigeitems.action.impl;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.ActionType;
import pers.neige.neigeitems.action.handler.SyncActionHandler;
import pers.neige.neigeitems.action.result.Results;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.lazy.ThreadSafeLazy;
import pers.neige.neigeitems.utils.lazy.ThreadSafeLazyBoolean;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public class StringAction extends Action {
    private final @NonNull String action;
    private final @NonNull String key;
    private final @NonNull String content;
    private final @NonNull ThreadSafeLazy<BiFunction<ActionContext, String, CompletableFuture<ActionResult>>> handler;

    public StringAction(
            @NonNull BaseActionManager manager,
            @NonNull String action
    ) {
        super(manager);
        this.action = action;
        val info = action.split(": ", 2);
        this.key = info[0].toLowerCase(Locale.ROOT);
        this.content = info.length > 1 ? info[1] : "";
        this.handler = new ThreadSafeLazy<>(() -> manager.getActions().get(this.key));
        checkAsyncSafe();
    }

    public StringAction(
            @NonNull BaseActionManager manager,
            @NonNull String action,
            @NonNull String key,
            @NonNull String content
    ) {
        super(manager);
        this.action = action;
        this.key = key;
        this.content = content;
        this.handler = new ThreadSafeLazy<>(() -> manager.getActions().get(this.key));
        checkAsyncSafe();
    }

    public StringAction(
            @NonNull BaseActionManager manager,
            @NonNull String key,
            @NonNull String content
    ) {
        this(manager, key + ": " + content, key, content);
    }

    private void checkAsyncSafe() {
        this.canRunInOtherThread = new ThreadSafeLazyBoolean(() -> !(this.handler.get() instanceof SyncActionHandler));
    }

    @Override
    public @NonNull ActionType getType() {
        return ActionType.STRING;
    }

    /**
     * 将基础类型动作的执行逻辑放入 BaseActionManager 是为了给其他插件覆写的机会
     */
    @Override
    protected @NonNull CompletableFuture<ActionResult> eval(
            @NonNull BaseActionManager manager,
            @NonNull ActionContext context
    ) {
        try {
            return manager.runAction(this, context);
        } catch (Throwable throwable) {
            manager.getPlugin().getLogger().warning("动作执行异常, 动作原始内容如下:");
            for (val actionLine : action.split("\n")) {
                manager.getPlugin().getLogger().warning(actionLine);
            }
            throwable.printStackTrace();
            return CompletableFuture.completedFuture(Results.STOP);
        }
    }

    @Override
    public @NonNull CompletableFuture<ActionResult> evalAsyncSafe(@NonNull BaseActionManager manager, @NonNull ActionContext context) {
        return super.evalAsyncSafe(manager, context);
    }

    public @NonNull String getAction() {
        return action;
    }

    public @NonNull String getKey() {
        return key;
    }

    public @NonNull String getContent() {
        return content;
    }

    public @Nullable BiFunction<ActionContext, String, CompletableFuture<ActionResult>> getHandler() {
        return handler.get();
    }
}
