package pers.neige.neigeitems.action;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.SchedulerUtils;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public abstract class Action {
    @NotNull
    protected final BaseActionManager manager;
    protected boolean asyncSafe = true;

    public Action(@NotNull BaseActionManager manager) {
        this.manager = manager;
    }

    @NotNull
    public ActionType getType() {
        return ActionType.UNKNOWN;
    }

    @NotNull
    protected abstract CompletableFuture<ActionResult> eval(
            @NotNull BaseActionManager manager,
            @NotNull ActionContext context
    );

    @NotNull
    public CompletableFuture<ActionResult> evalAsyncSafe(
            @NotNull BaseActionManager manager,
            @NotNull ActionContext context
    ) {
        if (this.asyncSafe) {
            // 如果线程状态不一致, 回归原始线程
            if (context.isSync() != Bukkit.isPrimaryThread()) {
                CompletableFuture<ActionResult> result = new CompletableFuture<>();
                SchedulerUtils.run(manager.getPlugin(), context.isSync(), () -> {
                    eval(manager, context).thenAccept(result::complete);
                });
                return result;
            }
            // 非主线程运行非线程安全动作, 进行线程切换
        } else {
            if (!Bukkit.isPrimaryThread()) {
                CompletableFuture<ActionResult> result = new CompletableFuture<>();
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

    @NotNull
    public CompletableFuture<ActionResult> eval(
            @NotNull ActionContext context
    ) {
        return evalAsyncSafe(manager, context);
    }

    /**
     * 向当前动作前插入一个动作, 并返回合并后的新动作.
     *
     * @param action 插入的动作
     * @return 新动作
     */
    @NotNull
    public Action insertBefore(@Nullable Object action) {
        return manager.compile(Arrays.asList(action, this));
    }

    /**
     * 向当前动作后插入一个动作, 并返回合并后的新动作.
     *
     * @param action 插入的动作
     * @return 新动作
     */
    @NotNull
    public Action insertAfter(@Nullable Object action) {
        return manager.compile(Arrays.asList(this, action));
    }
}
