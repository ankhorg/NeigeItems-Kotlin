package pers.neige.neigeitems.utils;

import lombok.NonNull;
import lombok.val;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.NeigeItems;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SchedulerUtils {
    /**
     * 在指定线程执行一段代码.
     *
     * @param task 执行的代码.
     */
    public static void run(
        boolean inPrimaryThread,
        @NonNull Runnable task
    ) {
        run(NeigeItems.getInstance(), inPrimaryThread, task);
    }

    /**
     * 在指定线程执行一段代码.
     *
     * @param plugin 注册任务的插件.
     * @param task   执行的代码.
     */
    public static void run(
        @NonNull Plugin plugin,
        boolean inPrimaryThread,
        @NonNull Runnable task
    ) {
        if (inPrimaryThread) {
            sync(plugin, task);
        } else {
            async(plugin, task);
        }
    }

    /**
     * 在主线程执行一段代码, 如果当前正处于主线程则直接执行, 如果不处在主线程则调用 runTask.
     *
     * @param task 执行的代码.
     */
    public static void sync(
        @NonNull Runnable task
    ) {
        sync(NeigeItems.getInstance(), task);
    }

    /**
     * 在主线程执行一段代码, 如果当前正处于主线程则直接执行, 如果不处在主线程则调用 runTask.
     *
     * @param plugin 注册任务的插件.
     * @param task   执行的代码.
     */
    public static void sync(
        @NonNull Plugin plugin,
        @NonNull Runnable task
    ) {
        if (Bukkit.isPrimaryThread()) {
            task.run();
        } else {
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }

    /**
     * 在主线程延迟执行一段代码.
     *
     * @param delay 延迟时间(tick).
     * @param task  执行的代码.
     */
    public static void syncLater(
        long delay,
        @NonNull Runnable task
    ) {
        syncLater(NeigeItems.getInstance(), delay, task);
    }

    /**
     * 在主线程延迟执行一段代码.
     *
     * @param plugin 注册任务的插件.
     * @param delay  延迟时间(tick).
     * @param task   执行的代码.
     */
    public static void syncLater(
        @NonNull Plugin plugin,
        long delay,
        @NonNull Runnable task
    ) {
        Bukkit.getScheduler().runTaskLater(plugin, task, delay);
    }

    /**
     * 在主线程重复执行一段代码.
     *
     * @param delay 延迟时间(tick).
     * @param task  执行的代码.
     */
    public static void syncTimer(
        long delay,
        long period,
        @NonNull Runnable task
    ) {
        syncTimer(NeigeItems.getInstance(), delay, period, task);
    }

    /**
     * 在主线程重复执行一段代码.
     *
     * @param plugin 注册任务的插件.
     * @param delay  延迟时间(tick).
     * @param task   执行的代码.
     */
    public static void syncTimer(
        @NonNull Plugin plugin,
        long delay,
        long period,
        @NonNull Runnable task
    ) {
        Bukkit.getScheduler().runTaskTimer(plugin, task, delay, period);
    }

    /**
     * 在主线程执行一段代码, 返回这段代码的返回值, 如果当前正处于主线程则直接执行, 如果不处在主线程则调用 callSyncMethod.
     *
     * @param task 执行的代码.
     */
    public static @Nullable <T> T syncAndGet(
        @NonNull Callable<T> task
    ) {
        return syncAndGet(NeigeItems.getInstance(), task);
    }

    /**
     * 在主线程执行一段代码, 返回这段代码的返回值, 如果当前正处于主线程则直接执行, 如果不处在主线程则调用 callSyncMethod.
     *
     * @param plugin 注册任务的插件.
     * @param task   执行的代码.
     */
    public static @Nullable <T> T syncAndGet(
        @NonNull Plugin plugin,
        @NonNull Callable<T> task
    ) {
        if (Bukkit.isPrimaryThread()) {
            try {
                return task.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                return Bukkit.getScheduler().callSyncMethod(plugin, task).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 在主线程执行一段代码, 返回这段代码的返回值, 如果当前正处于主线程则直接执行, 如果不处在主线程则调用 callSyncMethod.
     *
     * @param task 执行的代码.
     */
    public static @NonNull <T> CompletableFuture<T> callSyncMethod(
        @NonNull Callable<T> task
    ) {
        return callSyncMethod(NeigeItems.getInstance(), task);
    }

    /**
     * 在主线程执行一段代码, 返回这段代码的返回值, 如果当前正处于主线程则直接执行, 如果不处在主线程则调用 callSyncMethod.
     *
     * @param plugin 注册任务的插件.
     * @param task   执行的代码.
     */
    public static @NonNull <T> CompletableFuture<T> callSyncMethod(
        @NonNull Plugin plugin,
        @NonNull Callable<T> task
    ) {
        if (Bukkit.isPrimaryThread()) {
            try {
                return CompletableFuture.completedFuture(task.call());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            val future = new CompletableFuture<T>();
            Bukkit.getScheduler().runTask(plugin, () -> {
                try {
                    future.complete(task.call());
                } catch (Exception e) {
                    e.printStackTrace();
                    future.complete(null);
                }
            });
            return future;
        }
        return CompletableFuture.completedFuture(null);
    }

    /**
     * 异步执行一段代码.
     *
     * @param task 执行的代码.
     */
    public static void async(
        @NonNull Runnable task
    ) {
        async(NeigeItems.getInstance(), task);
    }

    /**
     * 异步执行一段代码.
     *
     * @param plugin 注册任务的插件.
     * @param task   执行的代码.
     */
    public static void async(
        @NonNull Plugin plugin,
        @NonNull Runnable task
    ) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
    }

    /**
     * 异步延迟执行一段代码.
     *
     * @param delay 延迟时间(tick).
     * @param task  执行的代码.
     */
    public static void asyncLater(
        long delay,
        @NonNull Runnable task
    ) {
        asyncLater(NeigeItems.getInstance(), delay, task);
    }

    /**
     * 异步延迟执行一段代码.
     *
     * @param plugin 注册任务的插件.
     * @param delay  延迟时间(tick).
     * @param task   执行的代码.
     */
    public static void asyncLater(
        @NonNull Plugin plugin,
        long delay,
        @NonNull Runnable task
    ) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, delay);
    }

    /**
     * 异步重复执行一段代码.
     *
     * @param delay 延迟时间(tick).
     * @param task  执行的代码.
     */
    public static void asyncTimer(
        long delay,
        long period,
        @NonNull Runnable task
    ) {
        asyncTimer(NeigeItems.getInstance(), delay, period, task);
    }

    /**
     * 异步重复执行一段代码.
     *
     * @param plugin 注册任务的插件.
     * @param delay  延迟时间(tick).
     * @param task   执行的代码.
     */
    public static void asyncTimer(
        @NonNull Plugin plugin,
        long delay,
        long period,
        @NonNull Runnable task
    ) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, delay, period);
    }

    /**
     * 在当前线程延迟执行一段代码.
     *
     * @param delay 延迟时间(tick).
     * @param task  执行的代码.
     */
    public static void runLater(
        long delay,
        @NonNull Runnable task
    ) {
        runLater(NeigeItems.getInstance(), delay, task);
    }

    /**
     * 在当前线程延迟执行一段代码.
     *
     * @param plugin 注册任务的插件.
     * @param delay  延迟时间(tick).
     * @param task   执行的代码.
     */
    public static void runLater(
        @NonNull Plugin plugin,
        long delay,
        @NonNull Runnable task
    ) {
        if (Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTaskLater(plugin, task, delay);
        } else {
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, delay);
        }
    }
}
