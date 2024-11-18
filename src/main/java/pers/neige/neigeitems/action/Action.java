package pers.neige.neigeitems.action;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.SchedulerUtils;

import java.util.concurrent.CompletableFuture;

public abstract class Action {
    protected boolean asyncSafe = true;

    @NotNull
    public ActionType getType() {
        return ActionType.UNKNOWN;
    }

    @NotNull
    public abstract CompletableFuture<ActionResult> eval(
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
}
