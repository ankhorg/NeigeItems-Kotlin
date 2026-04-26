package pers.neige.neigeitems;

import lombok.NonNull;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import pers.neige.neigeitems.utils.IdAllocator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link IdAllocator} 的完整测试用例
 */
@DisplayName("IdAllocator 测试")
class IdAllocatorTest {
    @Nested
    @DisplayName("基础功能测试")
    class BasicFunctionalityTests {
        private @NonNull IdAllocator allocator;

        @BeforeEach
        void setUp() {
            allocator = new IdAllocator();
        }

        @Test
        @DisplayName("空分配器应从 0 开始分配")
        void emptyAllocator_shouldStartFromZero() {
            assertEquals(0, allocator.acquire());
            assertEquals(1, allocator.acquire());
            assertEquals(2, allocator.acquire());
        }

        @Test
        @DisplayName("分配的 ID 应标记为已使用")
        void acquiredId_shouldBeMarkedAsUsed() {
            val id = allocator.acquire();
            assertTrue(allocator.isUsed(id));
        }

        @Test
        @DisplayName("释放的 ID 应可被重新分配")
        void releasedId_shouldBeReused() {
            val id1 = allocator.acquire(); // 0
            val id2 = allocator.acquire(); // 1
            val id3 = allocator.acquire(); // 2

            assertTrue(allocator.release(id2));
            assertFalse(allocator.isUsed(id2));

            // 下次分配应该返回刚释放的 ID 2
            val id4 = allocator.acquire();
            assertEquals(id2, id4);
        }

        @Test
        @DisplayName("释放未使用的 ID 应返回 false")
        void releaseUnusedId_shouldReturnFalse() {
            assertFalse(allocator.release(999));
        }

        @Test
        @DisplayName("重复释放同一 ID 应返回 false")
        void releaseSameIdTwice_shouldReturnFalse() {
            val id = allocator.acquire();
            assertTrue(allocator.release(id));
            assertFalse(allocator.release(id));
        }
    }

    @Nested
    @DisplayName("初始化测试")
    class InitializationTests {
        @Test
        @DisplayName("使用 {1, 2, 3} 初始化应从 4 开始分配")
        void initWithContinuousIds_shouldStartAfterMax() {
            val usedIds = new HashSet<>(Arrays.asList(1, 2, 3));
            val allocator = new IdAllocator(usedIds);

            assertEquals(4, allocator.acquire());
            assertEquals(5, allocator.acquire());
        }

        @Test
        @DisplayName("使用 {1, 3, 5} 初始化应先填充空洞")
        void initWithGaps_shouldFillGapsFirst() {
            val usedIds = new HashSet<>(Arrays.asList(1, 3, 5));
            val allocator = new IdAllocator(usedIds);

            assertEquals(2, allocator.acquire()); // 填充第一个空洞
            assertEquals(4, allocator.acquire()); // 填充第二个空洞
            assertEquals(6, allocator.acquire()); // 超过最大值
        }

        @Test
        @DisplayName("使用 {-2, 0, 2, 3} 初始化应从 -1 开始分配")
        void initWithGapsFromNegativeTwo_shouldStartFromNegativeOne() {
            Set<Integer> usedIds = new HashSet<>(Arrays.asList(-2, 0, 2, 3));
            IdAllocator allocator = new IdAllocator(usedIds);

            assertEquals(-1, allocator.acquire());
            assertEquals(1, allocator.acquire());
            assertEquals(4, allocator.acquire());
            assertEquals(5, allocator.acquire());
        }

        @Test
        @DisplayName("初始化后已用 ID 应正确标记")
        void afterInit_usedIdsShouldBeCorrect() {
            Set<Integer> usedIds = new HashSet<>(Arrays.asList(1, 3, 5));
            IdAllocator allocator = new IdAllocator(usedIds);

            assertTrue(allocator.isUsed(1));
            assertTrue(allocator.isUsed(3));
            assertTrue(allocator.isUsed(5));
            assertFalse(allocator.isUsed(2));
            assertFalse(allocator.isUsed(4));
            assertFalse(allocator.isUsed(6));
        }
    }

    @Nested
    @DisplayName("区间合并测试")
    class IntervalMergeTests {
        @Test
        @DisplayName("释放中间 ID 应合并前后区间")
        void releaseMiddleId_shouldMergeBothSides() {
            // 初始化：已用 ID 为 {1, 2, 4}
            val usedIds = new HashSet<>(Arrays.asList(1, 2, 4));
            val allocator = new IdAllocator(usedIds);

            val holesView = allocator.getHolesView();

            // 空洞为 [3, 3] 和 [5, MAX]
            assertEquals(2, allocator.getHoleCount());
            val iterator = holesView.entrySet().iterator();
            val first = iterator.next();
            assertEquals(3, first.getKey());
            assertEquals(3, first.getValue());
            val next = iterator.next();
            assertEquals(5, next.getKey());
            assertEquals(Integer.MAX_VALUE, next.getValue());

            // 释放 4
            allocator.release(4);
            // [3, 3] 应该与 [5, MAX] 合并，变为 [3, MAX]
            assertEquals(1, allocator.getHoleCount());
            val newHole = holesView.firstEntry();
            assertEquals(3, newHole.getKey());
            assertEquals(Integer.MAX_VALUE, newHole.getValue());
        }

        @Test
        @DisplayName("释放相邻 ID 应合并区间 - 向前合并")
        void releaseAdjacentId_shouldMergeForward() {
            // 初始化：已用 ID 为 {1, 2, 5, 6}
            val usedIds = new HashSet<>(Arrays.asList(1, 2, 5, 6));
            val allocator = new IdAllocator(usedIds);

            val holesView = allocator.getHolesView();

            // 空洞为 [3, 4] 和 [7, MAX]
            assertEquals(2, allocator.getHoleCount());
            val iterator = holesView.entrySet().iterator();
            val first = iterator.next();
            assertEquals(3, first.getKey());
            assertEquals(4, first.getValue());
            val next = iterator.next();
            assertEquals(7, next.getKey());
            assertEquals(Integer.MAX_VALUE, next.getValue());

            // 释放 5
            allocator.release(5);
            // [3, 4] 应该变为 [3, 5]
            assertEquals(2, allocator.getHoleCount());
            val newFirstHole = holesView.firstEntry();
            assertEquals(3, newFirstHole.getKey());
            assertEquals(5, newFirstHole.getValue());
        }

        @Test
        @DisplayName("释放相邻 ID 应合并区间 - 向后合并")
        void releaseAdjacentId_shouldMergeBackward() {
            // 初始化：已用 ID 为 {1, 2, 5, 6}, 空洞为 [3, 4] 和 [7, MAX]
            val usedIds = new HashSet<>(Arrays.asList(1, 2, 5, 6));
            val allocator = new IdAllocator(usedIds);

            val holesView = allocator.getHolesView();

            // 释放 6
            allocator.release(6);
            // [7, MAX] 应该变为 [6, MAX]
            assertEquals(2, allocator.getHoleCount());
            val newNextHole = holesView.lastEntry();
            assertEquals(6, newNextHole.getKey());
            assertEquals(Integer.MAX_VALUE, newNextHole.getValue());
        }

        @Test
        @DisplayName("释放孤立 ID 应创建新区间")
        void releaseIsolatedId_shouldCreateNewInterval() {
            // 初始化：已用 ID 为 {1, 2, 5, 6}, 空洞为 [3, 4] 和 [7, MAX]
            val usedIds = new HashSet<>(Arrays.asList(1, 2, 5, 6));
            val allocator = new IdAllocator(usedIds);

            val holesView = allocator.getHolesView();

            // 释放 1
            allocator.release(1);
            // 新增 [1, 1] 区间
            assertEquals(3, allocator.getHoleCount());
            val newFirstHole = holesView.firstEntry();
            assertEquals(1, newFirstHole.getKey());
            assertEquals(1, newFirstHole.getValue());
        }
    }

    @Nested
    @DisplayName("负向 ID 测试")
    class NegativeIdTests {
        @Test
        @DisplayName("正向ID耗尽后应进行负向分配")
        void whenPositiveExhausted_shouldAllocateNegative() {
            // 初始化：已用 ID 为 {MAX-1, MAX}, 正向已耗尽
            val usedIds = new HashSet<>(Arrays.asList(Integer.MAX_VALUE - 1, Integer.MAX_VALUE));
            val allocator = new IdAllocator(usedIds);

            // 不存在正向空洞
            assertEquals(0, allocator.getHoleCount());

            // 进行负方向分配
            assertEquals(Integer.MAX_VALUE - 2, allocator.acquire());
            assertEquals(Integer.MAX_VALUE - 3, allocator.acquire());
            assertEquals(Integer.MAX_VALUE - 4, allocator.acquire());
        }
    }

    // =========================
    // 边界条件测试
    // =========================

    @Nested
    @DisplayName("边界条件测试")
    class BoundaryTests {

        @Test
        @DisplayName("连续分配和释放应保持一致性")
        void continuousAcquireAndRelease_shouldBeConsistent() {
            val allocator = new IdAllocator();

            // 连续分配 100 个 ID, 验证分配的 ID 是 0-99
            for (int i = 0; i < 100; i++) {
                assertEquals(i, allocator.acquire());
            }

            // 释放所有偶数 ID
            for (int i = 0; i < 100; i += 2) {
                allocator.release(i);
            }

            // 再次分配 50 个 ID，应该是偶数
            for (int i = 0; i < 50; i++) {
                assertEquals(0, allocator.acquire() % 2);
            }
        }

        @Test
        @DisplayName("大量 ID 分配后状态应正确")
        void largeAmountOfIds_shouldBeCorrect() {
            val allocator = new IdAllocator();
            val count = 10000;

            // 分配大量 ID
            val allocated = new HashSet<Integer>();
            for (int i = 0; i < count; i++) {
                allocated.add(allocator.acquire());
            }

            // 验证数量
            assertEquals(count, allocated.size());
            assertEquals(count, allocator.getUsedCount());

            // 验证所有分配的 ID 都被标记为已使用
            for (val id : allocated) {
                assertTrue(allocator.isUsed(id));
            }
        }

        @Test
        @DisplayName("重置后应恢复到初始状态")
        void afterReset_shouldBeInInitialState() {
            val allocator = new IdAllocator();

            // 分配一些 ID
            for (int i = 0; i < 10; i++) {
                allocator.acquire();
            }

            // 重置
            allocator.reset();

            // 验证状态
            assertEquals(0, allocator.getUsedCount());
            assertEquals(0, allocator.acquire());
        }

        @Test
        @DisplayName("重新初始化应正确更新状态")
        void reinitialize_shouldUpdateCorrectly() {
            val allocator = new IdAllocator();

            // 初始分配
            for (int i = 0; i < 5; i++) {
                allocator.acquire();
            }

            // 重新初始化
            val newUsedIds = new HashSet<>(Arrays.asList(10, 20, 30));
            allocator.reinitialize(newUsedIds);

            // 验证状态
            assertEquals(3, allocator.getUsedCount());
            assertTrue(allocator.isUsed(10));
            assertTrue(allocator.isUsed(20));
            assertTrue(allocator.isUsed(30));

            // 下一个分配的应该是 11
            assertEquals(11, allocator.acquire());
        }
    }

    @Nested
    @DisplayName("查询方法测试")
    class QueryMethodTests {

        @Test
        @DisplayName("getUsedCount 应返回正确的数量")
        void getUsedCount_shouldReturnCorrectCount() {
            val allocator = new IdAllocator();

            assertEquals(0, allocator.getUsedCount());

            allocator.acquire();
            assertEquals(1, allocator.getUsedCount());

            allocator.acquire();
            allocator.acquire();
            assertEquals(3, allocator.getUsedCount());

            allocator.release(2);
            assertEquals(2, allocator.getUsedCount());
        }

        @Test
        @DisplayName("getHoleCount 应返回正确的区间数量")
        void getHoleCount_shouldReturnCorrectIntervalCount() {
            // 初始化：已用 ID 为 {1, 3, 5}，空洞为 [2,2], [4,4], [6,MAX]
            val usedIds = new HashSet<>(Arrays.asList(1, 3, 5));
            val allocator = new IdAllocator(usedIds);

            assertEquals(3, allocator.getHoleCount());
        }

        @Test
        @DisplayName("peekNextPositiveId 应返回下一个将被分配的 ID")
        void peekNextPositiveId_shouldReturnNextId() {
            val usedIds = new HashSet<>(Arrays.asList(1, 2, 3));
            val allocator = new IdAllocator(usedIds);

            assertEquals(4, allocator.peekNextId());

            // peek 不应改变状态
            assertEquals(4, allocator.peekNextId());
            assertEquals(4, allocator.acquire());
            assertEquals(5, allocator.peekNextId());
        }

        @Test
        @DisplayName("getHolesSnapshot 应返回正确的快照")
        void getHolesSnapshot_shouldReturnCorrectSnapshot() {
            val usedIds = new HashSet<>(Arrays.asList(1, 5));
            val allocator = new IdAllocator(usedIds);

            val snapshot = allocator.getHolesSnapshot();

            assertEquals(2, snapshot.size());
            assertEquals(4, snapshot.get(2)); // [2, 4]
            assertEquals(Integer.MAX_VALUE, snapshot.get(6)); // [6, MAX]
        }

        @Test
        @DisplayName("getUsedIdsSnapshot 应返回正确的快照")
        void getUsedIdsSnapshot_shouldReturnCorrectSnapshot() {
            val usedIds = new HashSet<>(Arrays.asList(1, 3, 5));
            val allocator = new IdAllocator(usedIds);

            val snapshot = allocator.getUsedIdsSnapshot();

            assertEquals(3, snapshot.size());
            assertTrue(snapshot.contains(1));
            assertTrue(snapshot.contains(3));
            assertTrue(snapshot.contains(5));
        }

        @Test
        @DisplayName("toString 应包含关键信息")
        void toString_shouldContainKeyInfo() {
            val usedIds = new HashSet<>(Arrays.asList(1, 2, 3));
            val allocator = new IdAllocator(usedIds);

            val str = allocator.toString();

            assertTrue(str.contains("usedCount=3"));
            assertTrue(str.contains("nextPositiveId=4"));
        }
    }

    @Nested
    @DisplayName("并发测试")
    class ConcurrencyTests {
        @Test
        @DisplayName("并发分配应返回唯一 ID")
        void concurrentAcquire_shouldReturnUniqueIds() throws InterruptedException {
            val allocator = new IdAllocator();
            val threadCount = 10;
            val idsPerThread = 100;
            val allIds = ConcurrentHashMap.newKeySet();

            try (val executor = Executors.newFixedThreadPool(threadCount)) {
                val latch = new CountDownLatch(threadCount);

                for (int i = 0; i < threadCount; i++) {
                    executor.submit(() -> {
                        try {
                            for (int j = 0; j < idsPerThread; j++) {
                                val id = allocator.acquire();
                                assertTrue(allIds.add(id), "重复的 ID: " + id);
                            }
                        } finally {
                            latch.countDown();
                        }
                    });
                }

                latch.await(10, TimeUnit.SECONDS);
                executor.shutdown();

                // 验证所有 ID 都是唯一的
                assertEquals(threadCount * idsPerThread, allIds.size());
            }
        }

        @Test
        @DisplayName("并发分配和释放应保持一致性")
        void concurrentAcquireAndRelease_shouldBeConsistent() throws InterruptedException {
            val allocator = new IdAllocator();
            val threadCount = 10;
            val operationsPerThread = 100;
            val allocateCount = new AtomicInteger(0);
            val releaseCount = new AtomicInteger(0);

            try (val executor = Executors.newFixedThreadPool(threadCount)) {
                val latch = new CountDownLatch(threadCount);

                for (int i = 0; i < threadCount; i++) {
                    executor.submit(() -> {
                        try {
                            val myIds = new ArrayList<Integer>();
                            for (int j = 0; j < operationsPerThread; j++) {
                                if (j % 3 == 0 && !myIds.isEmpty()) {
                                    // 释放一个 ID
                                    val id = myIds.remove(0);
                                    if (allocator.release(id)) {
                                        releaseCount.incrementAndGet();
                                    }
                                } else {
                                    // 分配一个 ID
                                    val id = allocator.acquire();
                                    myIds.add(id);
                                    allocateCount.incrementAndGet();
                                }
                            }
                        } finally {
                            latch.countDown();
                        }
                    });
                }

                latch.await(10, TimeUnit.SECONDS);
                executor.shutdown();

                // 验证最终状态一致
                val expectedUsed = allocateCount.get() - releaseCount.get();
                assertEquals(expectedUsed, allocator.getUsedCount());
            }
        }
    }

    @Nested
    @DisplayName("特殊场景测试")
    class SpecialScenarioTests {
        @Test
        @DisplayName("交替分配和释放同一 ID")
        void alternateAcquireAndRelease_sameId() {
            val allocator = new IdAllocator();

            for (int i = 0; i < 10; i++) {
                val id = allocator.acquire();
                assertEquals(0, id);
                allocator.release(id);
            }

            assertEquals(0, allocator.getUsedCount());
        }

        @Test
        @DisplayName("释放后立即分配应返回相同 ID")
        void releaseThenAcquire_shouldReturnSameId() {
            val allocator = new IdAllocator();

            // 分配 0-9
            for (int i = 0; i < 10; i++) {
                allocator.acquire();
            }

            // 释放 5
            allocator.release(5);

            // 下一个分配应该是 5
            assertEquals(5, allocator.acquire());
        }
    }
}
