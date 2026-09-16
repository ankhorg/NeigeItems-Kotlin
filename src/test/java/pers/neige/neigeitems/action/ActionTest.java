package pers.neige.neigeitems.action;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import pers.neige.neigeitems.manager.BaseActionManager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ActionTest {
    private Plugin plugin;
    private BaseActionManager manager;
    private BukkitScheduler scheduler;
    private MockedStatic<Bukkit> mockedBukkit;

    @BeforeEach
    void setUp() {
        plugin = mock(Plugin.class);
        manager = mock(BaseActionManager.class);
        scheduler = mock(BukkitScheduler.class);
        when(manager.getPlugin()).thenReturn(plugin);
        mockedBukkit = Mockito.mockStatic(Bukkit.class);
        mockedBukkit.when(Bukkit::getScheduler).thenReturn(scheduler);
    }

    @AfterEach
    void tearDown() {
        mockedBukkit.close();
    }

    @Test
    void directFutureSuccessAndFailureAreForwarded() {
        mockedBukkit.when(Bukkit::isPrimaryThread).thenReturn(true);

        CompletableFuture<ActionResult> delayed = new CompletableFuture<>();
        TestAction delayedAction = new TestAction(manager, true, () -> delayed);
        CompletableFuture<ActionResult> delayedResult = delayedAction.evalAsyncSafe(manager, new ActionContext());
        assertFalse(delayedResult.isDone());
        delayed.complete(pers.neige.neigeitems.action.result.Results.SUCCESS);
        assertSame(pers.neige.neigeitems.action.result.Results.SUCCESS, delayedResult.join());

        IllegalStateException failure = new IllegalStateException("inner failure");
        TestAction failedAction = new TestAction(manager, true, () -> failedFuture(failure));
        CompletableFuture<ActionResult> failedResult = failedAction.evalAsyncSafe(manager, new ActionContext());
        assertSame(failure, failureOf(failedResult));
    }

    @Test
    void directEvalExceptionBecomesExceptionalFuture() {
        mockedBukkit.when(Bukkit::isPrimaryThread).thenReturn(true);
        IllegalStateException failure = new IllegalStateException("eval failure");
        TestAction action = new TestAction(manager, true, () -> {
            throw failure;
        });

        CompletableFuture<ActionResult> result = assertDoesNotThrow(
            () -> action.evalAsyncSafe(manager, new ActionContext())
        );
        assertSame(failure, failureOf(result));
    }

    @Test
    void innerCancellationTerminatesOuterFuture() {
        mockedBukkit.when(Bukkit::isPrimaryThread).thenReturn(true);
        CompletableFuture<ActionResult> inner = new CompletableFuture<>();
        TestAction action = new TestAction(manager, true, () -> inner);

        CompletableFuture<ActionResult> result = action.evalAsyncSafe(manager, new ActionContext());
        assertTrue(inner.cancel(false));
        assertTrue(result.isDone());
        assertTrue(result.isCancelled());
        assertThrows(CancellationException.class, result::join);
    }

    @Test
    void syncSwitchForwardsFailureAndUsesSyncScheduler() {
        mockedBukkit.when(Bukkit::isPrimaryThread).thenReturn(false);
        AtomicReference<Runnable> scheduled = new AtomicReference<>();
        doAnswer(invocation -> {
            scheduled.set(invocation.getArgument(1));
            return mock(BukkitTask.class);
        }).when(scheduler).runTask(eq(plugin), any(Runnable.class));

        IllegalStateException failure = new IllegalStateException("scheduled failure");
        TestAction action = new TestAction(manager, true, () -> failedFuture(failure));
        ActionContext context = new ActionContext();
        context.setSync(true);

        CompletableFuture<ActionResult> result = action.evalAsyncSafe(manager, context);
        assertFalse(result.isDone());
        verify(scheduler).runTask(eq(plugin), any(Runnable.class));
        scheduled.get().run();
        assertSame(failure, failureOf(result));
    }

    @Test
    void asyncSwitchUsesAsyncSchedulerAndForwardsSuccess() {
        mockedBukkit.when(Bukkit::isPrimaryThread).thenReturn(true);
        AtomicReference<Runnable> scheduled = new AtomicReference<>();
        doAnswer(invocation -> {
            scheduled.set(invocation.getArgument(1));
            return mock(BukkitTask.class);
        }).when(scheduler).runTaskAsynchronously(eq(plugin), any(Runnable.class));

        TestAction action = new TestAction(
            manager,
            true,
            () -> CompletableFuture.completedFuture(pers.neige.neigeitems.action.result.Results.SUCCESS)
        );
        ActionContext context = new ActionContext();
        context.setSync(false);

        CompletableFuture<ActionResult> result = action.evalAsyncSafe(manager, context);
        assertFalse(result.isDone());
        verify(scheduler).runTaskAsynchronously(eq(plugin), any(Runnable.class));
        scheduled.get().run();
        assertSame(pers.neige.neigeitems.action.result.Results.SUCCESS, result.join());
    }

    @Test
    void nonThreadSafeActionSwitchesFromAsyncThreadToMainThread() {
        mockedBukkit.when(Bukkit::isPrimaryThread).thenReturn(false);
        AtomicReference<Runnable> scheduled = new AtomicReference<>();
        doAnswer(invocation -> {
            scheduled.set(invocation.getArgument(1));
            return mock(BukkitTask.class);
        }).when(scheduler).runTask(eq(plugin), any(Runnable.class));

        TestAction action = new TestAction(
            manager,
            false,
            () -> CompletableFuture.completedFuture(pers.neige.neigeitems.action.result.Results.SUCCESS)
        );

        CompletableFuture<ActionResult> result = action.evalAsyncSafe(manager, new ActionContext());
        assertFalse(result.isDone());
        verify(scheduler).runTask(eq(plugin), any(Runnable.class));
        scheduled.get().run();
        assertSame(pers.neige.neigeitems.action.result.Results.SUCCESS, result.join());
    }

    @Test
    void schedulerSubmissionExceptionBecomesExceptionalFuture() {
        mockedBukkit.when(Bukkit::isPrimaryThread).thenReturn(false);
        IllegalStateException failure = new IllegalStateException("submission failure");
        doThrow(failure).when(scheduler).runTask(eq(plugin), any(Runnable.class));

        TestAction action = new TestAction(
            manager,
            true,
            () -> CompletableFuture.completedFuture(pers.neige.neigeitems.action.result.Results.SUCCESS)
        );
        ActionContext context = new ActionContext();
        context.setSync(true);

        CompletableFuture<ActionResult> result = assertDoesNotThrow(
            () -> action.evalAsyncSafe(manager, context)
        );
        assertSame(failure, failureOf(result));
        assertEquals(0, action.getCalls());
    }

    private static Throwable failureOf(CompletableFuture<?> future) {
        CompletionException exception = assertThrows(CompletionException.class, future::join);
        return exception.getCause();
    }

    private static <T> CompletableFuture<T> failedFuture(Throwable failure) {
        CompletableFuture<T> result = new CompletableFuture<>();
        result.completeExceptionally(failure);
        return result;
    }

    private static final class TestAction extends Action {
        private final boolean asyncSafe;
        private final Supplier<CompletableFuture<ActionResult>> evaluation;
        private int calls;

        private TestAction(
            BaseActionManager manager,
            boolean asyncSafe,
            Supplier<CompletableFuture<ActionResult>> evaluation
        ) {
            super(manager);
            this.asyncSafe = asyncSafe;
            this.evaluation = evaluation;
        }

        @Override
        public boolean canRunInOtherThread() {
            return asyncSafe;
        }

        @Override
        protected CompletableFuture<ActionResult> eval(
            BaseActionManager manager,
            ActionContext context
        ) {
            calls++;
            return evaluation.get();
        }

        private int getCalls() {
            return calls;
        }
    }
}
