package pers.neige.neigeitems.utils;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.NeigeItems;
import pers.neige.neigeitems.action.ActionContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

/**
 * Bukkit/Paper/Folia scheduler compatibility layer.
 *
 * <p>Folia does not have one main thread. Calls that belong to an entity are
 * therefore routed through that entity's scheduler; calls without an entity
 * are routed through Folia's global-region scheduler.</p>
 */
public class SchedulerUtils {
    private static final String FOLIA_SERVER_CLASS = "io.papermc.paper.threadedregions.RegionizedServer";
    private static volatile @Nullable Boolean folia;

    public SchedulerUtils() {
    }

    /** A small cancellation abstraction that works on Bukkit and Folia. */
    public interface TaskHandle {
        void cancel();
    }

    private static final class BukkitTaskHandle implements TaskHandle {
        private final BukkitTask task;

        private BukkitTaskHandle(@NonNull BukkitTask task) {
            this.task = task;
        }

        @Override
        public void cancel() {
            task.cancel();
        }
    }

    private static final class ReflectedTaskHandle implements TaskHandle {
        private final Object task;

        private ReflectedTaskHandle(@Nullable Object task) {
            this.task = task;
        }

        @Override
        public void cancel() {
            if (task == null) return;
            try {
                invoke(task, "cancel", new Class<?>[0]);
            } catch (Throwable error) {
                error.printStackTrace();
            }
        }
    }

    private static boolean isFolia() {
        Boolean value = folia;
        if (value != null) return value;
        synchronized (SchedulerUtils.class) {
            value = folia;
            if (value == null) {
                try {
                    Class.forName(FOLIA_SERVER_CLASS, false, SchedulerUtils.class.getClassLoader());
                    value = true;
                } catch (Throwable ignored) {
                    value = false;
                }
                folia = value;
            }
        }
        return value;
    }

    /** Exposes the runtime choice without linking NI to Folia API classes. */
    public static boolean isFoliaServer() {
        return isFolia();
    }

    private static Object invoke(
        @NonNull Object target,
        @NonNull String name,
        @NonNull Class<?>[] parameterTypes,
        Object... arguments
    ) throws Throwable {
        try {
            Method method = target.getClass().getMethod(name, parameterTypes);
            // Folia 返回的调度任务实现类不是 public，直接 invoke 会被 Java 访问检查拒绝。
            method.setAccessible(true);
            return method.invoke(target, arguments);
        } catch (InvocationTargetException error) {
            throw error.getCause() == null ? error : error.getCause();
        }
    }

    private static Object globalScheduler() throws Throwable {
        return invoke(Bukkit.getServer(), "getGlobalRegionScheduler", new Class<?>[0]);
    }

    private static Object asyncScheduler() throws Throwable {
        return invoke(Bukkit.getServer(), "getAsyncScheduler", new Class<?>[0]);
    }

    private static Object entityScheduler(@NonNull Entity entity) throws Throwable {
        return invoke(entity, "getScheduler", new Class<?>[0]);
    }

    private static Consumer<Object> callback(@NonNull Runnable task) {
        return ignored -> task.run();
    }

    private static long ticks(long ticks) {
        return Math.max(0L, ticks);
    }

    private static long positiveTicks(long ticks) {
        return Math.max(1L, ticks);
    }

    private static long millis(long ticks) {
        return Math.max(0L, ticks) * 50L;
    }

    private static TaskHandle scheduleGlobalFolia(
        @NonNull Plugin plugin,
        long delay,
        long period,
        @NonNull Runnable task
    ) {
        try {
            Object scheduler = globalScheduler();
            Consumer<Object> callback = callback(task);
            Object scheduled;
            if (period > 0) {
                scheduled = invoke(
                    scheduler,
                    "runAtFixedRate",
                    new Class<?>[]{Plugin.class, Consumer.class, long.class, long.class},
                    plugin,
                    callback,
                    positiveTicks(delay),
                    positiveTicks(period)
                );
            } else if (delay > 0) {
                scheduled = invoke(
                    scheduler,
                    "runDelayed",
                    new Class<?>[]{Plugin.class, Consumer.class, long.class},
                    plugin,
                    callback,
                    delay
                );
            } else {
                scheduled = invoke(
                    scheduler,
                    "run",
                    new Class<?>[]{Plugin.class, Consumer.class},
                    plugin,
                    callback
                );
            }
            return new ReflectedTaskHandle(scheduled);
        } catch (Throwable error) {
            throw new IllegalStateException("Unable to schedule a Folia global-region task", error);
        }
    }

    private static TaskHandle scheduleAsyncFolia(
        @NonNull Plugin plugin,
        long delay,
        long period,
        @NonNull Runnable task
    ) {
        try {
            Object scheduler = asyncScheduler();
            Consumer<Object> callback = callback(task);
            Object scheduled;
            if (period > 0) {
                scheduled = invoke(
                    scheduler,
                    "runAtFixedRate",
                    new Class<?>[]{Plugin.class, Consumer.class, long.class, long.class, TimeUnit.class},
                    plugin,
                    callback,
                    millis(delay),
                    Math.max(50L, millis(period)),
                    TimeUnit.MILLISECONDS
                );
            } else if (delay > 0) {
                scheduled = invoke(
                    scheduler,
                    "runDelayed",
                    new Class<?>[]{Plugin.class, Consumer.class, long.class, TimeUnit.class},
                    plugin,
                    callback,
                    millis(delay),
                    TimeUnit.MILLISECONDS
                );
            } else {
                scheduled = invoke(
                    scheduler,
                    "runNow",
                    new Class<?>[]{Plugin.class, Consumer.class},
                    plugin,
                    callback
                );
            }
            return new ReflectedTaskHandle(scheduled);
        } catch (Throwable error) {
            throw new IllegalStateException("Unable to schedule a Folia asynchronous task", error);
        }
    }

    private static TaskHandle scheduleEntityFolia(
        @NonNull Plugin plugin,
        @NonNull Entity entity,
        long delay,
        long period,
        @NonNull Runnable task
    ) {
        return scheduleEntityFolia(plugin, entity, delay, period, task, null);
    }

    private static TaskHandle scheduleEntityFolia(
        @NonNull Plugin plugin,
        @NonNull Entity entity,
        long delay,
        long period,
        @NonNull Runnable task,
        @Nullable Runnable retired
    ) {
        try {
            Object scheduler = entityScheduler(entity);
            Consumer<Object> callback = callback(task);
            Object scheduled;
            if (period > 0) {
                scheduled = invoke(
                    scheduler,
                    "runAtFixedRate",
                    new Class<?>[]{Plugin.class, Consumer.class, Runnable.class, long.class, long.class},
                    plugin,
                    callback,
                    retired,
                    positiveTicks(delay),
                    positiveTicks(period)
                );
            } else {
                scheduled = invoke(
                    scheduler,
                    "runDelayed",
                    new Class<?>[]{Plugin.class, Consumer.class, Runnable.class, long.class},
                    plugin,
                    callback,
                    retired,
                    positiveTicks(delay)
                );
            }
            return new ReflectedTaskHandle(scheduled);
        } catch (Throwable error) {
            throw new IllegalStateException("Unable to schedule a Folia entity task", error);
        }
    }

    /**
     * Schedules work on an entity's owning thread without blocking the caller.
     *
     * <p>This is required for Netty callbacks on Folia: waiting for an entity
     * task from a channel event-loop thread can deadlock the packet write.</p>
     */
    public static void executeOnEntity(
        @NonNull Plugin plugin,
        @NonNull Entity entity,
        @NonNull Runnable task,
        @Nullable Runnable retired
    ) {
        if (!isFolia() || isOwnedByCurrentRegion(entity)) {
            task.run();
            return;
        }
        scheduleEntityFolia(plugin, entity, 0, 0, task, retired);
    }

    private static boolean isOwnedByCurrentRegion(@NonNull Entity entity) {
        if (!isFolia()) return Bukkit.isPrimaryThread();
        try {
            return (Boolean) invoke(
                Bukkit.getServer(),
                "isOwnedByCurrentRegion",
                new Class<?>[]{Entity.class},
                entity
            );
        } catch (Throwable ignored) {
            return false;
        }
    }

    private static boolean isOnEntityRegion(@Nullable ActionContext context) {
        if (context == null || !(context.getCaster() instanceof Entity)) return false;
        return isOwnedByCurrentRegion((Entity) context.getCaster());
    }

    /** 在实体所属的 Folia 区域线程同步执行并取得结果。 */
    public static @Nullable <T> T callOnEntity(
        @NonNull Plugin plugin,
        @NonNull Entity entity,
        @NonNull Callable<T> task
    ) {
        if (!isFolia()) return syncAndGet(plugin, task);
        if (isOwnedByCurrentRegion(entity)) {
            try {
                return task.call();
            } catch (Throwable error) {
                error.printStackTrace();
                return null;
            }
        }
        CompletableFuture<T> future = new CompletableFuture<>();
        try {
            scheduleEntityFolia(plugin, entity, 1, 0, () -> {
                try {
                    future.complete(task.call());
                } catch (Throwable error) {
                    future.completeExceptionally(error);
                }
            });
            return future.get(5L, TimeUnit.SECONDS);
        } catch (InterruptedException error) {
            Thread.currentThread().interrupt();
        } catch (ExecutionException | TimeoutException error) {
            error.printStackTrace();
        } catch (Throwable error) {
            error.printStackTrace();
        }
        return null;
    }

    /** Whether the current thread is already suitable for this action context. */
    public static boolean isOnSyncThread(@Nullable ActionContext context) {
        return isFolia() ? isOnEntityRegion(context) : Bukkit.isPrimaryThread();
    }

    /** Whether the current thread is Folia's global region thread. */
    public static boolean isOnGlobalRegionThread() {
        if (!isFolia()) return Bukkit.isPrimaryThread();
        try {
            Class<?> serverClass = Class.forName(FOLIA_SERVER_CLASS, false, SchedulerUtils.class.getClassLoader());
            Method method = serverClass.getMethod("isGlobalTickThread");
            method.setAccessible(true);
            return (Boolean) method.invoke(null);
        } catch (Throwable ignored) {
            return false;
        }
    }

    private static @Nullable Entity entityOf(@Nullable ActionContext context) {
        if (context == null) return null;
        Object caster = context.getCaster();
        return caster instanceof Entity ? (Entity) caster : null;
    }

    /** Schedule a repeating task on one entity's owning region. */
    public static @NonNull TaskHandle entityTimer(
        @NonNull Plugin plugin,
        @NonNull Entity entity,
        long delay,
        long period,
        @NonNull Runnable task
    ) {
        if (isFolia()) {
            return scheduleEntityFolia(plugin, entity, delay, period, task);
        }
        if (period > 0) {
            return new BukkitTaskHandle(Bukkit.getScheduler().runTaskTimer(plugin, task, ticks(delay), ticks(period)));
        }
        return new BukkitTaskHandle(Bukkit.getScheduler().runTaskLater(plugin, task, ticks(delay)));
    }

    /** Schedule a plugin-owned task without using BukkitRunnable. */
    public static @NonNull TaskHandle schedule(
        @NonNull Plugin plugin,
        boolean async,
        long delay,
        long period,
        @NonNull Runnable task
    ) {
        if (isFolia()) {
            return async
                ? scheduleAsyncFolia(plugin, delay, period, task)
                : scheduleGlobalFolia(plugin, delay, period, task);
        }
        if (async) {
            if (period > 0) {
                return new BukkitTaskHandle(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, ticks(delay), ticks(period)));
            }
            if (delay > 0) {
                return new BukkitTaskHandle(Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, ticks(delay)));
            }
            return new BukkitTaskHandle(Bukkit.getScheduler().runTaskAsynchronously(plugin, task));
        }
        if (period > 0) {
            return new BukkitTaskHandle(Bukkit.getScheduler().runTaskTimer(plugin, task, ticks(delay), ticks(period)));
        }
        if (delay > 0) {
            return new BukkitTaskHandle(Bukkit.getScheduler().runTaskLater(plugin, task, ticks(delay)));
        }
        return new BukkitTaskHandle(Bukkit.getScheduler().runTask(plugin, task));
    }

    /** 在指定线程执行一段代码. */
    public static void run(boolean inPrimaryThread, @NonNull Runnable task) {
        run(NeigeItems.getInstance(), inPrimaryThread, task);
    }

    /** 在指定线程执行一段代码. */
    public static void run(@NonNull Plugin plugin, boolean inPrimaryThread, @NonNull Runnable task) {
        if (inPrimaryThread) {
            sync(plugin, task);
        } else {
            async(plugin, task);
        }
    }

    /** Schedule according to both the requested thread and the action owner. */
    public static void run(
        @NonNull Plugin plugin,
        @Nullable ActionContext context,
        boolean inPrimaryThread,
        @NonNull Runnable task
    ) {
        if (inPrimaryThread) {
            sync(plugin, context, task);
        } else {
            async(plugin, task);
        }
    }

    public static void sync(@NonNull Runnable task) {
        sync(NeigeItems.getInstance(), task);
    }

    public static void sync(@NonNull Plugin plugin, @NonNull Runnable task) {
        if (isFolia()) {
            scheduleGlobalFolia(plugin, 0, 0, task);
        } else if (Bukkit.isPrimaryThread()) {
            task.run();
        } else {
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }

    /** Schedule a synchronous action on its entity, or on Folia's global region. */
    public static void sync(
        @NonNull Plugin plugin,
        @Nullable ActionContext context,
        @NonNull Runnable task
    ) {
        Entity entity = entityOf(context);
        if (isFolia()) {
            if (entity != null) {
                if (isOnEntityRegion(context)) {
                    task.run();
                } else {
                    scheduleEntityFolia(plugin, entity, 1, 0, task);
                }
            } else {
                scheduleGlobalFolia(plugin, 0, 0, task);
            }
        } else if (Bukkit.isPrimaryThread()) {
            task.run();
        } else {
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }

    public static void syncLater(long delay, @NonNull Runnable task) {
        syncLater(NeigeItems.getInstance(), delay, task);
    }

    public static void syncLater(@NonNull Plugin plugin, long delay, @NonNull Runnable task) {
        if (isFolia()) {
            scheduleGlobalFolia(plugin, delay, 0, task);
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, task, ticks(delay));
        }
    }

    public static void syncLater(
        @NonNull Plugin plugin,
        @Nullable ActionContext context,
        long delay,
        @NonNull Runnable task
    ) {
        Entity entity = entityOf(context);
        if (isFolia() && entity != null) {
            scheduleEntityFolia(plugin, entity, delay, 0, task);
        } else {
            syncLater(plugin, delay, task);
        }
    }

    public static void syncTimer(long delay, long period, @NonNull Runnable task) {
        syncTimer(NeigeItems.getInstance(), delay, period, task);
    }

    public static void syncTimer(
        @NonNull Plugin plugin,
        long delay,
        long period,
        @NonNull Runnable task
    ) {
        if (isFolia()) {
            scheduleGlobalFolia(plugin, delay, period, task);
        } else {
            Bukkit.getScheduler().runTaskTimer(plugin, task, ticks(delay), ticks(period));
        }
    }

    public static @Nullable <T> T syncAndGet(@NonNull Callable<T> task) {
        return syncAndGet(NeigeItems.getInstance(), task);
    }

    public static @Nullable <T> T syncAndGet(@NonNull Plugin plugin, @NonNull Callable<T> task) {
        if (!isFolia() && Bukkit.isPrimaryThread()) {
            try {
                return task.call();
            } catch (Exception error) {
                error.printStackTrace();
            }
            return null;
        }
        try {
            return callSyncMethod(plugin, task).get();
        } catch (InterruptedException error) {
            Thread.currentThread().interrupt();
            error.printStackTrace();
        } catch (ExecutionException error) {
            error.printStackTrace();
        }
        return null;
    }

    public static @NonNull <T> CompletableFuture<T> callSyncMethod(@NonNull Callable<T> task) {
        return callSyncMethod(NeigeItems.getInstance(), task);
    }

    public static @NonNull <T> CompletableFuture<T> callSyncMethod(
        @NonNull Plugin plugin,
        @NonNull Callable<T> task
    ) {
        if (!isFolia() && Bukkit.isPrimaryThread()) {
            try {
                return CompletableFuture.completedFuture(task.call());
            } catch (Exception error) {
                error.printStackTrace();
                return CompletableFuture.completedFuture(null);
            }
        }
        CompletableFuture<T> future = new CompletableFuture<>();
        sync(plugin, () -> {
            try {
                future.complete(task.call());
            } catch (Exception error) {
                future.completeExceptionally(error);
            }
        });
        return future;
    }

    public static void async(@NonNull Runnable task) {
        async(NeigeItems.getInstance(), task);
    }

    public static void async(@NonNull Plugin plugin, @NonNull Runnable task) {
        if (isFolia()) {
            scheduleAsyncFolia(plugin, 0, 0, task);
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
        }
    }

    public static void asyncLater(long delay, @NonNull Runnable task) {
        asyncLater(NeigeItems.getInstance(), delay, task);
    }

    public static void asyncLater(@NonNull Plugin plugin, long delay, @NonNull Runnable task) {
        if (isFolia()) {
            scheduleAsyncFolia(plugin, delay, 0, task);
        } else {
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, ticks(delay));
        }
    }

    public static void asyncTimer(long delay, long period, @NonNull Runnable task) {
        asyncTimer(NeigeItems.getInstance(), delay, period, task);
    }

    public static void asyncTimer(
        @NonNull Plugin plugin,
        long delay,
        long period,
        @NonNull Runnable task
    ) {
        if (isFolia()) {
            scheduleAsyncFolia(plugin, delay, period, task);
        } else {
            Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, ticks(delay), ticks(period));
        }
    }

    public static void runLater(long delay, @NonNull Runnable task) {
        runLater(NeigeItems.getInstance(), delay, task);
    }

    public static void runLater(@NonNull Plugin plugin, long delay, @NonNull Runnable task) {
        if (isFolia()) {
            asyncLater(plugin, delay, task);
        } else if (Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTaskLater(plugin, task, ticks(delay));
        } else {
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, ticks(delay));
        }
    }

    public static void runLater(
        @NonNull Plugin plugin,
        @NonNull ActionContext context,
        long delay,
        @NonNull Runnable task
    ) {
        if (context.isSync()) {
            syncLater(plugin, context, delay, task);
        } else {
            asyncLater(plugin, delay, task);
        }
    }
}
