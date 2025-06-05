package pers.neige.neigeitems.action;

import lombok.NonNull;
import lombok.val;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.SchedulerUtils;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public abstract class Action {
    protected final @NonNull BaseActionManager manager;
    protected boolean asyncSafe = true;

    public Action(@NonNull BaseActionManager manager) {
        this.manager = manager;
    }

    public @NonNull ActionType getType() {
        return ActionType.UNKNOWN;
    }

    protected abstract @NonNull CompletableFuture<ActionResult> eval(
            @NonNull BaseActionManager manager,
            @NonNull ActionContext context
    );

    public @NonNull CompletableFuture<ActionResult> evalAsyncSafe(
            @NonNull BaseActionManager manager,
            @NonNull ActionContext context
    ) {
        if (this.asyncSafe) {
            // 如果线程状态不一致, 回归原始线程
            if (context.isSync() != Bukkit.isPrimaryThread()) {
                val result = new CompletableFuture<ActionResult>();
                SchedulerUtils.run(manager.getPlugin(), context.isSync(), () -> {
                    eval(manager, context).thenAccept(result::complete);
                });
                return result;
            }
            // 非主线程运行非线程安全动作, 进行线程切换
        } else {
            if (!Bukkit.isPrimaryThread()) {
                val result = new CompletableFuture<ActionResult>();
                // 转主线程
                Bukkit.getScheduler().runTask(manager.getPlugin(), () -> {
                    eval(manager, context).thenAccept(result::complete);
                });
                return result;
            }
        }
        return eval(manager, context);
    }

    public boolean isAsyncSafe() {
        return asyncSafe;
    }

    public @NonNull CompletableFuture<ActionResult> evalAsyncSafe(
            @NonNull ActionContext context
    ) {
        return evalAsyncSafe(manager, context);
    }

    public @NonNull CompletableFuture<ActionResult> eval(
            @NonNull ActionContext context
    ) {
        return evalAsyncSafe(manager, context.clone());
    }

    /**
     * 向当前动作前插入一个动作, 并返回合并后的新动作.
     *
     * @param action 插入的动作
     * @return 新动作
     */
    public @NonNull Action insertBefore(@Nullable Object action) {
        return manager.compile(Arrays.asList(action, this));
    }

    /**
     * 向当前动作后插入一个动作, 并返回合并后的新动作.
     *
     * @param action 插入的动作
     * @return 新动作
     */
    public @NonNull Action insertAfter(@Nullable Object action) {
        return manager.compile(Arrays.asList(this, action));
    }
}
