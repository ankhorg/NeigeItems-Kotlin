package pers.neige.neigeitems.manager;

import org.bukkit.Bukkit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import pers.neige.neigeitems.action.Action;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.ActionResult;
import pers.neige.neigeitems.action.Condition;
import pers.neige.neigeitems.action.impl.ListAction;
import pers.neige.neigeitems.action.impl.RepeatAction;
import pers.neige.neigeitems.action.impl.WhileAction;
import pers.neige.neigeitems.action.result.Results;
import pers.neige.neigeitems.config.ConfigReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BaseActionManagerTest {
    private MockedStatic<Bukkit> mockedBukkit;
    private BaseActionManager manager;

    @BeforeEach
    void setUp() {
        mockedBukkit = Mockito.mockStatic(Bukkit.class);
        mockedBukkit.when(Bukkit::isPrimaryThread).thenReturn(true);
        manager = mock(BaseActionManager.class, Mockito.CALLS_REAL_METHODS);
    }

    @AfterEach
    void tearDown() {
        mockedBukkit.close();
    }

    @Test
    void listWithOneHundredThousandImmediateActionsIsStackSafe() throws Exception {
        AtomicInteger calls = new AtomicInteger();
        ControlledAction child = new ControlledAction(manager, () -> {
            calls.incrementAndGet();
            return CompletableFuture.completedFuture(Results.SUCCESS);
        });
        List<Action> children = new ArrayList<>(100_000);
        for (int i = 0; i < 100_000; i++) children.add(child);

        ListAction action = new ListAction(manager, children);
        ActionResult result = manager.runAction(action, new ActionContext()).get(5, TimeUnit.SECONDS);

        assertSame(Results.SUCCESS, result);
        assertEquals(100_000, calls.get());
    }

    @Test
    void repeatWithOneHundredThousandImmediateActionsIsStackSafeAndKeepsLastResult() throws Exception {
        AtomicInteger calls = new AtomicInteger();
        ActionResult last = new TestResult();
        ControlledAction child = new ControlledAction(manager, () -> {
            calls.incrementAndGet();
            return CompletableFuture.completedFuture(last);
        });
        Map<String, Object> config = new HashMap<>();
        config.put("global-id", "iteration");
        config.put("repeat", 100_000);
        config.put("actions", child);

        RepeatAction action = new RepeatAction(manager, ConfigReader.parse(config));
        ActionContext context = new ActionContext();
        ActionResult result = manager.runAction(action, context).get(5, TimeUnit.SECONDS);

        assertSame(last, result);
        assertEquals(100_000, calls.get());
        assertEquals(99_999, context.getGlobal().get("iteration"));
    }

    @Test
    void whileWithOneHundredThousandImmediateActionsIsStackSafe() throws Exception {
        AtomicInteger calls = new AtomicInteger();
        AtomicInteger conditions = new AtomicInteger();
        ControlledAction body = new ControlledAction(manager, () -> {
            calls.incrementAndGet();
            return CompletableFuture.completedFuture(Results.SUCCESS);
        });
        ControlledAction finallyAction = new ControlledAction(
            manager,
            () -> CompletableFuture.completedFuture(Results.SUCCESS)
        );
        Condition condition = mock(Condition.class);
        when(condition.easyCheck(any(ActionContext.class))).thenAnswer(invocation -> conditions.getAndIncrement() < 100_000);
        WhileAction action = mock(WhileAction.class);
        when(action.getCondition()).thenReturn(condition);
        when(action.getActions()).thenReturn(body);
        when(action.getFinally()).thenReturn(finallyAction);

        ActionResult result = manager.runAction(action, new ActionContext()).get(5, TimeUnit.SECONDS);

        assertSame(Results.SUCCESS, result);
        assertEquals(100_000, calls.get());
        assertEquals(1, finallyAction.getCalls());
    }

    @Test
    void whileRunsFinallyForStopAndReturnsFinallyResult() throws Exception {
        ActionResult finallyResult = new TestResult();
        ControlledAction body = new ControlledAction(
            manager,
            () -> CompletableFuture.completedFuture(Results.STOP)
        );
        ControlledAction finallyAction = new ControlledAction(
            manager,
            () -> CompletableFuture.completedFuture(finallyResult)
        );
        Condition condition = mock(Condition.class);
        when(condition.easyCheck(any(ActionContext.class))).thenReturn(true);
        WhileAction action = mock(WhileAction.class);
        when(action.getCondition()).thenReturn(condition);
        when(action.getActions()).thenReturn(body);
        when(action.getFinally()).thenReturn(finallyAction);

        ActionResult result = manager.runAction(action, new ActionContext()).get(5, TimeUnit.SECONDS);

        assertSame(finallyResult, result);
        assertEquals(1, body.getCalls());
        assertEquals(1, finallyAction.getCalls());
    }

    @Test
    void listStopsWithoutExecutingFollowingActions() throws Exception {
        AtomicInteger followingCalls = new AtomicInteger();
        ControlledAction stop = new ControlledAction(
            manager,
            () -> CompletableFuture.completedFuture(Results.STOP)
        );
        ControlledAction following = new ControlledAction(manager, () -> {
            followingCalls.incrementAndGet();
            return CompletableFuture.completedFuture(Results.SUCCESS);
        });
        ListAction action = new ListAction(manager, java.util.Arrays.asList(stop, following));

        assertSame(Results.STOP, manager.runAction(action, new ActionContext()).get(5, TimeUnit.SECONDS));
        assertEquals(0, followingCalls.get());
    }

    @Test
    void listPropagatesChildExceptionWithoutExecutingFollowingActions() {
        AtomicInteger followingCalls = new AtomicInteger();
        IllegalStateException failure = new IllegalStateException("list failure");
        ControlledAction failed = new ControlledAction(manager, () -> failedFuture(failure));
        ControlledAction following = new ControlledAction(manager, () -> {
            followingCalls.incrementAndGet();
            return CompletableFuture.completedFuture(Results.SUCCESS);
        });
        ListAction action = new ListAction(manager, java.util.Arrays.asList(failed, following));

        CompletionException exception = assertThrows(
            CompletionException.class,
            () -> manager.runAction(action, new ActionContext()).join()
        );
        assertSame(failure, exception.getCause());
        assertEquals(0, followingCalls.get());
    }

    @Test
    void delayedActionResumesOnceAndRegistrationRaceDoesNotDuplicate() throws Exception {
        CompletableFuture<ActionResult> delayed = new CompletableFuture<>();
        AtomicInteger calls = new AtomicInteger();
        ControlledAction first = new ControlledAction(manager, () -> delayed);
        ControlledAction second = new ControlledAction(manager, () -> {
            calls.incrementAndGet();
            return new CompleteOnRegistrationFuture<>(Results.SUCCESS);
        });
        ListAction action = new ListAction(manager, java.util.Arrays.asList(first, second));
        CompletableFuture<ActionResult> result = manager.runAction(action, new ActionContext());

        assertFalse(result.isDone());
        assertEquals(0, calls.get());
        delayed.complete(Results.SUCCESS);

        assertSame(Results.SUCCESS, result.get(5, TimeUnit.SECONDS));
        assertEquals(1, calls.get());
    }

    @Test
    void emptyListAndNonPositiveRepeatKeepCompatibilityResults() throws Exception {
        ListAction empty = new ListAction(manager, Collections.emptyList());
        assertSame(Results.SUCCESS, manager.runAction(empty, new ActionContext()).get(5, TimeUnit.SECONDS));

        AtomicInteger calls = new AtomicInteger();
        ControlledAction child = new ControlledAction(manager, () -> {
            calls.incrementAndGet();
            return CompletableFuture.completedFuture(Results.SUCCESS);
        });
        Map<String, Object> config = new HashMap<>();
        config.put("repeat", 0);
        config.put("actions", child);
        RepeatAction repeat = new RepeatAction(manager, ConfigReader.parse(config));

        assertSame(Results.SUCCESS, manager.runAction(repeat, new ActionContext()).get(5, TimeUnit.SECONDS));
        assertEquals(0, calls.get());
    }

    private static final class ControlledAction extends Action {
        private final Supplier<CompletableFuture<ActionResult>> evaluation;
        private int calls;

        private ControlledAction(BaseActionManager manager, Supplier<CompletableFuture<ActionResult>> evaluation) {
            super(manager);
            this.evaluation = evaluation;
        }

        @Override
        public boolean canRunInOtherThread() {
            return true;
        }

        @Override
        protected CompletableFuture<ActionResult> eval(BaseActionManager manager, ActionContext context) {
            calls++;
            return evaluation.get();
        }

        private int getCalls() {
            return calls;
        }
    }

    private static final class CompleteOnRegistrationFuture<T> extends CompletableFuture<T> {
        private final T value;

        private CompleteOnRegistrationFuture(T value) {
            this.value = value;
        }

        @Override
        public CompletableFuture<T> whenComplete(java.util.function.BiConsumer<? super T, ? super Throwable> action) {
            complete(value);
            return super.whenComplete(action);
        }
    }

    private static <T> CompletableFuture<T> failedFuture(Throwable failure) {
        CompletableFuture<T> result = new CompletableFuture<>();
        result.completeExceptionally(failure);
        return result;
    }

    private static final class TestResult extends ActionResult {
        @Override
        public pers.neige.neigeitems.action.ResultType getType() {
            return pers.neige.neigeitems.action.ResultType.SUCCESS;
        }
    }
}
