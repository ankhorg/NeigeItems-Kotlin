package pers.neige.neigeitems.utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.NeigeItems;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class SchedulerUtils {
    /**
     * 在主线程执行一段代码, 如果当前正处于主线程则直接执行, 如果不处在主线程则调用 runTask.
     *
     * @param task 执行的代码.
     */
    public static void sync(
            @NotNull Runnable task
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
            @NotNull Plugin plugin,
            @NotNull Runnable task
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
            @NotNull Runnable task
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
            @NotNull Plugin plugin,
            long delay,
            @NotNull Runnable task
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
            @NotNull Runnable task
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
            @NotNull Plugin plugin,
            long delay,
            long period,
            @NotNull Runnable task
    ) {
        Bukkit.getScheduler().runTaskTimer(plugin, task, delay, period);
    }

    /**
     * 在主线程执行一段代码, 返回这段代码的返回值, 如果当前正处于主线程则直接执行, 如果不处在主线程则调用 callSyncMethod.
     *
     * @param task 执行的代码.
     */
    @Nullable
    public static <T> T syncAndGet(
            @NotNull Callable<T> task
    ) {
        return syncAndGet(NeigeItems.getInstance(), task);
    }

    /**
     * 在主线程执行一段代码, 返回这段代码的返回值, 如果当前正处于主线程则直接执行, 如果不处在主线程则调用 callSyncMethod.
     *
     * @param plugin 注册任务的插件.
     * @param task   执行的代码.
     */
    @Nullable
    public static <T> T syncAndGet(
            @NotNull Plugin plugin,
            @NotNull Callable<T> task
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
     * 异步执行一段代码.
     *
     * @param task 执行的代码.
     */
    public static void async(
            @NotNull Runnable task
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
            @NotNull Plugin plugin,
            @NotNull Runnable task
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
            @NotNull Runnable task
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
            @NotNull Plugin plugin,
            long delay,
            @NotNull Runnable task
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
            @NotNull Runnable task
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
            @NotNull Plugin plugin,
            long delay,
            long period,
            @NotNull Runnable task
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
            @NotNull Runnable task
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
            @NotNull Plugin plugin,
            long delay,
            @NotNull Runnable task
    ) {
        if (Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTaskLater(plugin, task, delay);
        } else {
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, delay);
        }
    }
}
