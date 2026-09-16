package pers.neige.neigeitems.action;

import lombok.NonNull;
import lombok.val;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.SchedulerUtils;
import pers.neige.neigeitems.utils.lazy.ThreadSafeLazyBoolean;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public abstract class Action {
    protected final @NonNull BaseActionManager manager;
    protected @Nullable ThreadSafeLazyBoolean canRunInOtherThread = null;

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
        if (canRunInOtherThread()) {
            // 如果线程状态不一致, 回归原始线程
            if (context.isSync() != Bukkit.isPrimaryThread()) {
                val result = new CompletableFuture<ActionResult>();
                try {
                    SchedulerUtils.run(manager.getPlugin(), context.isSync(), () -> evalAndForward(result, manager, context));
                } catch (Throwable error) {
                    completeExceptionally(result, error);
                }
                return result;
            }
            // 非主线程运行非线程安全动作, 进行线程切换
        } else {
            if (!Bukkit.isPrimaryThread()) {
                val result = new CompletableFuture<ActionResult>();
                // 转主线程
                try {
                    Bukkit.getScheduler().runTask(manager.getPlugin(), () -> evalAndForward(result, manager, context));
                } catch (Throwable error) {
                    completeExceptionally(result, error);
                }
                return result;
            }
        }
        val result = new CompletableFuture<ActionResult>();
        evalAndForward(result, manager, context);
        return result;
    }

    private void evalAndForward(
        @NonNull CompletableFuture<ActionResult> result,
        @NonNull BaseActionManager manager,
        @NonNull ActionContext context
    ) {
        try {
            val evaluated = eval(manager, context);
            if (evaluated == null) {
                throw new NullPointerException("Action.eval() returned null");
            }
            evaluated.whenComplete((value, error) -> {
                if (error == null) {
                    result.complete(value);
                } else if (evaluated.isCancelled()) {
                    result.cancel(false);
                } else {
                    completeExceptionally(result, error);
                }
            });
        } catch (Throwable error) {
            completeExceptionally(result, error);
        }
    }

    private static void completeExceptionally(
        @NonNull CompletableFuture<?> result,
        @NonNull Throwable error
    ) {
        result.completeExceptionally(error);
    }

    /**
     * 当前动作是否可以在非主线程运行.<br>
     * 假设当前动作是单一动作, 比如 String 类型动作.<br>
     * 那么此方法应反映当前动作的运行逻辑.<br>
     * 假设当前动作是多个动作的组合, 比如 List 类型动作.<br>
     * 那么, 只有组合内所有动作均需要在主线程运行时, 当前动作组合才需要在主线程运行.<br>
     * 或者说, 只要组合内存在不需要在主线程运行的动作, 就认定该动作组合不需要在主线程运行.<br>
     * 具体原因与线程切换判断有关, 是为了防止混合动作执行过程中频繁地反复横跳.
     */
    public boolean canRunInOtherThread() {
        return canRunInOtherThread == null || canRunInOtherThread.get();
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
