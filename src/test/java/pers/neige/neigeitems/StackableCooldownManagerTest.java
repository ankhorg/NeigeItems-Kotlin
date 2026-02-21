package pers.neige.neigeitems;

import lombok.NonNull;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pers.neige.neigeitems.utils.cooldown.StackableCooldown;
import pers.neige.neigeitems.utils.cooldown.StackableCooldownManager;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StackableCooldownManager累计次数冷却功能测试")
public class StackableCooldownManagerTest {
    private final @NonNull String TEST_KEY = "TEST";
    private final int MAX_AMOUNT = 3; // 最大累计 3 次
    private final long COOLDOWN = 10000; // 10 秒充能一次
    private final @NonNull StackableCooldown.Config TEST_CONFIG_5 = new StackableCooldown.Config(5, MAX_AMOUNT, COOLDOWN);
    private final @NonNull StackableCooldown.Config TEST_CONFIG_3 = new StackableCooldown.Config(MAX_AMOUNT, COOLDOWN);
    private final @NonNull StackableCooldown.Config TEST_CONFIG_1 = new StackableCooldown.Config(1, MAX_AMOUNT, COOLDOWN);
    private final @NonNull StackableCooldown.Config TEST_CONFIG_M1 = new StackableCooldown.Config(-1, MAX_AMOUNT, COOLDOWN);
    private final long START_TIME = System.currentTimeMillis();
    private StackableCooldownManager manager;

    @BeforeEach
    void setUp() {
        manager = new StackableCooldownManager();
    }

    @Nested
    @DisplayName("初始化测试")
    class InitTest {
        @Test
        @DisplayName("满充能初始化")
        void init3() {
            try (val mockedClass = Mockito.mockStatic(StackableCooldown.class)) {
                // 固定时间
                mockedClass.when(StackableCooldown::currentTimeMillis).thenReturn(START_TIME);

                val cooldown = manager.computeIfAbsent(TEST_KEY, TEST_CONFIG_3);
                assertEquals(MAX_AMOUNT, cooldown.getCooldownStatus().getAmount());
                assertEquals(0, cooldown.getCooldownStatus().getRemainingRecoveryTime());
            }
        }

        @Test
        @DisplayName("1次充能初始化")
        void init1() {
            try (val mockedClass = Mockito.mockStatic(StackableCooldown.class)) {
                // 固定时间
                mockedClass.when(StackableCooldown::currentTimeMillis).thenReturn(START_TIME);

                val cooldown = manager.computeIfAbsent(TEST_KEY, TEST_CONFIG_1);
                assertEquals(1, cooldown.getCooldownStatus().getAmount());
                assertEquals(COOLDOWN, cooldown.getCooldownStatus().getRemainingRecoveryTime());
            }
        }

        @Test
        @DisplayName("初始次数大于最大次数, 将被限制回最大次数")
        void init5() {
            try (val mockedClass = Mockito.mockStatic(StackableCooldown.class)) {
                // 固定时间
                mockedClass.when(StackableCooldown::currentTimeMillis).thenReturn(START_TIME);

                val cooldown = manager.computeIfAbsent(TEST_KEY, TEST_CONFIG_5);
                assertEquals(MAX_AMOUNT, cooldown.getCooldownStatus().getAmount());
                assertEquals(0, cooldown.getCooldownStatus().getRemainingRecoveryTime());
            }
        }

        @Test
        @DisplayName("初始次数小于0, 将被限制回0")
        void initM1() {
            try (val mockedClass = Mockito.mockStatic(StackableCooldown.class)) {
                // 固定时间
                mockedClass.when(StackableCooldown::currentTimeMillis).thenReturn(START_TIME);

                val cooldown = manager.computeIfAbsent(TEST_KEY, TEST_CONFIG_M1);
                assertEquals(0, cooldown.getCooldownStatus().getAmount());
                assertEquals(COOLDOWN, cooldown.getCooldownStatus().getRemainingRecoveryTime());
            }
        }
    }

    @Nested
    @DisplayName("消耗和状态查询")
    class ConsumeAndStatusTest {
        @Test
        @DisplayName("消耗流程：检查 ChargeResult 的返回值")
        void consume() {
            try (val mockedClass = Mockito.mockStatic(StackableCooldown.class)) {
                // 固定时间
                mockedClass.when(StackableCooldown::currentTimeMillis).thenReturn(START_TIME);
                val cooldown = manager.computeIfAbsent(TEST_KEY, TEST_CONFIG_3);

                // 消耗 1 次
                val result1 = cooldown.consume();

                assertTrue(result1.isSuccess(), "第一次应成功消耗");
                assertEquals(MAX_AMOUNT - 1, result1.getStatus().getAmount(), "消耗后剩余次数应减一");
                assertEquals(COOLDOWN, result1.getStatus().getRemainingRecoveryTime(), "消耗后恢复时间应为 10000ms");

                // 时间流逝 5 秒
                long time5Sec = START_TIME + 5000;
                mockedClass.when(StackableCooldown::currentTimeMillis).thenReturn(time5Sec);

                val status = cooldown.getCooldownStatus();
                assertEquals(MAX_AMOUNT - 1, status.getAmount(), "经过 5 秒后次数应无变化");
                assertEquals(5000, status.getRemainingRecoveryTime(), "经过 5 秒后恢复时间应为 5000ms");

                // 再消耗 2 次
                val result2 = cooldown.consume(2);

                assertTrue(result2.isSuccess(), "剩余次数应足够再消耗 2 次");
                assertEquals(0, result2.getStatus().getAmount(), "消耗后应无剩余次数");
                assertEquals(5000, result2.getStatus().getRemainingRecoveryTime(), "时间未推进, 因此恢复时间应仍为 5000ms");

                // 再消耗 1 次
                val result3 = cooldown.consume();

                assertFalse(result3.isSuccess(), "剩余 0 次, 因此本次消耗应失败");
                assertEquals(0, result3.getStatus().getAmount(), "消耗后应无剩余次数");
                assertEquals(5000, result3.getStatus().getRemainingRecoveryTime(), "时间未推进, 因此恢复时间应仍为 5000ms");
            }
        }

        @Test
        @DisplayName("getCooldownStatus 应返回正确的状态")
        void getCooldownStatus() {
            try (val mockedClass = Mockito.mockStatic(StackableCooldown.class)) {
                // 固定时间
                mockedClass.when(StackableCooldown::currentTimeMillis).thenReturn(START_TIME);
                val cooldown = manager.computeIfAbsent(TEST_KEY, TEST_CONFIG_3);

                // 消耗 1 次
                cooldown.consume();

                val status1 = cooldown.getCooldownStatus();
                assertEquals(MAX_AMOUNT - 1, status1.getAmount(), "消耗 1 次后应剩余 2 次");
                assertEquals(COOLDOWN, status1.getRemainingRecoveryTime(), "消耗后恢复时间应为 10000ms");

                // 时间流逝 5 秒
                long time5Sec = START_TIME + 5000;
                mockedClass.when(StackableCooldown::currentTimeMillis).thenReturn(time5Sec);

                val status2 = cooldown.getCooldownStatus();
                assertEquals(MAX_AMOUNT - 1, status2.getAmount(), "经过 5 秒后使用次数应无变化");
                assertEquals(5000, status2.getRemainingRecoveryTime(), "5 秒后剩余时间应为 5000ms");

                // 再消耗 1 次
                cooldown.consume();

                val status3 = cooldown.getCooldownStatus();
                assertEquals(MAX_AMOUNT - 2, status3.getAmount(), "消耗后变为 1 次");
                assertEquals(5000, status3.getRemainingRecoveryTime(), "时间未推进, 因此恢复时间应仍为 5000ms");

                // 时间流逝 8 秒
                long time8Sec = time5Sec + 8000;
                mockedClass.when(StackableCooldown::currentTimeMillis).thenReturn(time8Sec);

                val status4 = cooldown.getCooldownStatus();
                assertEquals(MAX_AMOUNT - 1, status4.getAmount(), "经过 8 秒后使用次数应加一");
                assertEquals(7000, status4.getRemainingRecoveryTime(), "8 秒后剩余时间应为 7000ms");

                // 时间流逝 100 秒
                long time100Sec = time8Sec + 100000;
                mockedClass.when(StackableCooldown::currentTimeMillis).thenReturn(time100Sec);

                val status5 = cooldown.getCooldownStatus();
                assertEquals(MAX_AMOUNT, status5.getAmount(), "100 秒后次数应回满");
                assertEquals(0, status5.getRemainingRecoveryTime(), "100 秒后次数应回满, 恢复时间为 0");
            }
        }
    }

    @Nested
    @DisplayName("冷却操作方法测试")
    class StackableCooldownManipulationTest {
        @Test
        @DisplayName("缩减冷却时间")
        void reduceCooldown() {
            try (val mockedClass = Mockito.mockStatic(StackableCooldown.class)) {
                // 固定时间
                mockedClass.when(StackableCooldown::currentTimeMillis).thenReturn(START_TIME);

                val cooldown = manager.computeIfAbsent(TEST_KEY, TEST_CONFIG_3);

                // 消耗 1 次, 剩余 2 次, 剩余时间 10000ms
                cooldown.consume();

                // 时间流逝 2 秒
                long time2Sec = START_TIME + 2000;
                mockedClass.when(StackableCooldown::currentTimeMillis).thenReturn(time2Sec);

                val status1 = cooldown.getCooldownStatus();
                assertEquals(MAX_AMOUNT - 1, status1.getAmount(), "消耗 1 次");
                assertEquals(COOLDOWN - 2000, status1.getRemainingRecoveryTime(), "剩余 8000ms");

                // 扣除 3 秒冷却时间
                cooldown.increaseOrReduceCooldown(-3000);

                val status2 = cooldown.getCooldownStatus();
                assertEquals(MAX_AMOUNT - 1, status2.getAmount(), "消耗 1 次");
                assertEquals(COOLDOWN - 2000 - 3000, status2.getRemainingRecoveryTime(), "剩余 5000ms");

                // 消耗 1 次, 剩余 1 次, 剩余时间 50000ms
                cooldown.consume();

                val status3 = cooldown.getCooldownStatus();
                assertEquals(MAX_AMOUNT - 2, status3.getAmount(), "消耗 2 次");
                assertEquals(COOLDOWN - 2000 - 3000, status3.getRemainingRecoveryTime(), "剩余 5000ms");

                // 扣除 11 秒冷却时间
                cooldown.increaseOrReduceCooldown(-11000);

                val status4 = cooldown.getCooldownStatus();
                assertEquals(MAX_AMOUNT - 1, status4.getAmount(), "剩余 2 次");
                assertEquals(COOLDOWN - 2000 - 3000 - 1000, status4.getRemainingRecoveryTime(), "剩余 4000ms");
            }
        }

        @Test
        @DisplayName("延长冷却时间")
        void increaseCooldown() {
            try (val mockedClass = Mockito.mockStatic(StackableCooldown.class)) {
                // 固定时间
                mockedClass.when(StackableCooldown::currentTimeMillis).thenReturn(START_TIME);

                val cooldown = manager.computeIfAbsent(TEST_KEY, TEST_CONFIG_3);

                // 消耗 1 次, 剩余 2 次, 剩余时间 10000ms
                cooldown.consume();

                // 延长 5 秒冷却时间
                cooldown.increaseOrReduceCooldown(5000);

                val status1 = cooldown.getCooldownStatus();
                assertEquals(MAX_AMOUNT - 1, status1.getAmount(), "消耗 1 次");
                assertEquals(COOLDOWN, status1.getRemainingRecoveryTime(), "延长冷却时间无法超出最大冷却时间");

                // 时间流逝 5 秒
                long time5Sec = START_TIME + 5000;
                mockedClass.when(StackableCooldown::currentTimeMillis).thenReturn(time5Sec);

                val status2 = cooldown.getCooldownStatus();
                assertEquals(MAX_AMOUNT - 1, status2.getAmount(), "经过 5 秒后次数应无变化");
                assertEquals(5000, status2.getRemainingRecoveryTime(), "经过 5 秒后恢复时间应为 5000ms");

                // 延长 5 秒冷却时间
                cooldown.increaseOrReduceCooldown(3000);

                val status3 = cooldown.getCooldownStatus();
                assertEquals(MAX_AMOUNT - 1, status3.getAmount(), "延长 5 秒后次数应无变化");
                assertEquals(8000, status3.getRemainingRecoveryTime(), "延长 5 秒后恢复时间应为 8000ms");
            }
        }

        @Test
        @DisplayName("设置冷却时间")
        void setCooldown() {
            try (val mockedClass = Mockito.mockStatic(StackableCooldown.class)) {
                // 固定时间
                mockedClass.when(StackableCooldown::currentTimeMillis).thenReturn(START_TIME);

                val cooldown = manager.computeIfAbsent(TEST_KEY, TEST_CONFIG_3);

                // 消耗 1 次, 剩余 2 次, 剩余时间 10000ms
                cooldown.consume();

                // 将冷却设置为 5000ms
                cooldown.setCooldown(5000);

                val status1 = cooldown.getCooldownStatus();
                assertEquals(MAX_AMOUNT - 1, status1.getAmount(), "消耗 1 次");
                assertEquals(5000, status1.getRemainingRecoveryTime(), "剩余 5000ms");

                // 将冷却设置为 15000ms
                cooldown.setCooldown(15000);

                val status2 = cooldown.getCooldownStatus();
                assertEquals(MAX_AMOUNT - 1, status2.getAmount(), "消耗 1 次");
                assertEquals(COOLDOWN, status2.getRemainingRecoveryTime(), "冷却不应超过最大冷却");

                // 将冷却设置为 0ms
                cooldown.setCooldown(0);

                val status3 = cooldown.getCooldownStatus();
                assertEquals(MAX_AMOUNT, status3.getAmount(), "将冷却设置为 0, 次数将立即恢复");
                assertEquals(0, status3.getRemainingRecoveryTime(), "次数已满, 恢复时间为 0");

                // 消耗 1 次, 剩余 2 次, 剩余时间 10000ms
                cooldown.consume();

                val status4 = cooldown.getCooldownStatus();
                assertEquals(MAX_AMOUNT - 1, status4.getAmount(), "消耗 1 次");
                assertEquals(COOLDOWN, status4.getRemainingRecoveryTime(), "剩余 10000ms");

                // 将冷却设置为 -10000ms
                cooldown.setCooldown(-10000);

                val status5 = cooldown.getCooldownStatus();
                assertEquals(MAX_AMOUNT, status5.getAmount(), "将冷却设置为小于等于 0 的数值, 次数将立即恢复");
                assertEquals(0, status5.getRemainingRecoveryTime(), "次数已满, 恢复时间为 0");
            }
        }

        @Test
        @DisplayName("增加次数")
        void addAmount() {
            try (val mockedClass = Mockito.mockStatic(StackableCooldown.class)) {
                // 固定时间
                mockedClass.when(StackableCooldown::currentTimeMillis).thenReturn(START_TIME);

                val cooldown = manager.computeIfAbsent(TEST_KEY, TEST_CONFIG_3);

                // 消耗 2 次
                cooldown.consume(2);
                // 增加 1 次
                cooldown.addOrTakeAmount(1);

                val status1 = cooldown.getCooldownStatus();
                assertEquals(MAX_AMOUNT - 2 + 1, status1.getAmount(), "消耗 2 次, 恢复 1 次");
                assertEquals(COOLDOWN, status1.getRemainingRecoveryTime(), "次数不满, 恢复时间为 10000ms");

                // 增加 5 次 (超出最大值 3)
                cooldown.addOrTakeAmount(5);

                val status2 = cooldown.getCooldownStatus();
                assertEquals(MAX_AMOUNT, status2.getAmount(), "最终次数不应超过最大值");
                assertEquals(0, status2.getRemainingRecoveryTime(), "满充能后，冷却时间应归零");
            }
        }

        @Test
        @DisplayName("减少次数")
        void takeAmount() {
            try (val mockedClass = Mockito.mockStatic(StackableCooldown.class)) {
                // 固定时间
                mockedClass.when(StackableCooldown::currentTimeMillis).thenReturn(START_TIME);

                val cooldown = manager.computeIfAbsent(TEST_KEY, TEST_CONFIG_3);

                // 扣除 1 次
                cooldown.addOrTakeAmount(-1);

                val status1 = cooldown.getCooldownStatus();
                assertEquals(MAX_AMOUNT - 1, status1.getAmount(), "扣除 1 次");
                assertEquals(COOLDOWN, status1.getRemainingRecoveryTime(), "次数不满, 恢复时间为 10000ms");

                // 扣除 5 次 (超限)
                cooldown.addOrTakeAmount(-5);

                val status2 = cooldown.getCooldownStatus();
                assertEquals(0, status2.getAmount(), "剩余次数不应低于零");
                assertEquals(COOLDOWN, status2.getRemainingRecoveryTime(), "次数不满, 恢复时间为 10000ms");
            }
        }

        @Test
        @DisplayName("设置次数")
        void setAmount() {
            try (val mockedClass = Mockito.mockStatic(StackableCooldown.class)) {
                // 固定时间
                mockedClass.when(StackableCooldown::currentTimeMillis).thenReturn(START_TIME);

                val cooldown = manager.computeIfAbsent(TEST_KEY, TEST_CONFIG_3);

                // 设置为 1 次
                cooldown.setAmount(1);

                val status1 = cooldown.getCooldownStatus();
                assertEquals(1, status1.getAmount(), "设置为 1 次");
                assertEquals(COOLDOWN, status1.getRemainingRecoveryTime(), "次数不满, 恢复时间为 10000ms");

                // 设置为 1 次
                cooldown.setAmount(-1);

                val status2 = cooldown.getCooldownStatus();
                assertEquals(0, status2.getAmount(), "剩余次数不应低于零");
                assertEquals(COOLDOWN, status2.getRemainingRecoveryTime(), "次数不满, 恢复时间为 10000ms");

                // 设置为 100 次
                cooldown.setAmount(100);

                val status3 = cooldown.getCooldownStatus();
                assertEquals(MAX_AMOUNT, status3.getAmount(), "剩余次数不应超过最大次数");
                assertEquals(0, status3.getRemainingRecoveryTime(), "次数已满, 恢复时间为 0");
            }
        }
    }

    @Nested
    @DisplayName("配置操作方法测试")
    class ConfigManipulationTest {
        @Test
        @DisplayName("设置最大使用次数")
        void setMaxAmount() {
            try (val mockedClass = Mockito.mockStatic(StackableCooldown.class)) {
                // 固定时间
                mockedClass.when(StackableCooldown::currentTimeMillis).thenReturn(START_TIME);

                val cooldown = manager.computeIfAbsent(TEST_KEY, TEST_CONFIG_3);

                // 将最大次数设置为 2 次
                cooldown.setMaxAmount(2);

                val status1 = cooldown.getCooldownStatus();
                assertEquals(2, status1.getAmount(), "剩余次数不会超过最大次数");
                assertEquals(0, status1.getRemainingRecoveryTime(), "次数满时剩余时间为 0");

                // 将最大次数设置为 3 次
                cooldown.setMaxAmount(3);

                val status2 = cooldown.getCooldownStatus();
                assertEquals(2, status2.getAmount(), "最大次数变大不会影响剩余次数");
                assertEquals(COOLDOWN, status2.getRemainingRecoveryTime(), "次数不满, 剩余时间变为 10000ms");

                // 时间流逝 5 秒
                long time5Sec = START_TIME + 5000;
                mockedClass.when(StackableCooldown::currentTimeMillis).thenReturn(time5Sec);

                val status3 = cooldown.getCooldownStatus();
                assertEquals(2, status3.getAmount(), "剩余 2 次");
                assertEquals(COOLDOWN - 5000, status3.getRemainingRecoveryTime(), "剩余时间 5000ms");

                // 将最大次数设置为 2 次
                cooldown.setMaxAmount(2);

                val status4 = cooldown.getCooldownStatus();
                assertEquals(2, status4.getAmount(), "剩余次数不会超过最大次数");
                assertEquals(0, status4.getRemainingRecoveryTime(), "次数满时剩余时间为 0");

                // 将最大次数设置为 -1 次
                cooldown.setMaxAmount(-1);

                val status5 = cooldown.getCooldownStatus();
                assertEquals(1, status5.getAmount(), "最大次数无法被设置为小于 1 的值");
                assertEquals(0, status5.getRemainingRecoveryTime(), "次数满时剩余时间为 0");
            }
        }

        @Test
        @DisplayName("设置刷新冷却")
        void setConfigCooldown() {
            try (val mockedClass = Mockito.mockStatic(StackableCooldown.class)) {
                // 固定时间
                mockedClass.when(StackableCooldown::currentTimeMillis).thenReturn(START_TIME);

                val cooldown = manager.computeIfAbsent(TEST_KEY, TEST_CONFIG_3);

                // 消耗 1 次
                cooldown.consume();

                val status1 = cooldown.getCooldownStatus();
                assertEquals(MAX_AMOUNT - 1, status1.getAmount(), "剩余 2 次");
                assertEquals(COOLDOWN, status1.getRemainingRecoveryTime(), "剩余时间 10000ms");

                // 设置刷新冷却为 5000ms
                cooldown.setConfigCooldown(5000);

                val status2 = cooldown.getCooldownStatus();
                assertEquals(MAX_AMOUNT - 1, status2.getAmount(), "剩余 2 次");
                assertEquals(5000, status2.getRemainingRecoveryTime(), "剩余时间不会超过刷新冷却");
            }
        }
    }
}
