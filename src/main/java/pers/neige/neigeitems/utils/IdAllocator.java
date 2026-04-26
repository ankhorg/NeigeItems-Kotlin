package pers.neige.neigeitems.utils;

import lombok.NonNull;
import lombok.val;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 通用 ID 分配器
 * <p>
 * 用于高效地分配和回收整数 ID。支持正向优先分配，正向耗尽后自动切换到负向。
 * 使用区间合并算法来高效管理空闲 ID 区间，确保 O(log n) 的分配和释放操作。
 * </p>
 *
 * <h3>特性：</h3>
 * <ul>
 *   <li>正向优先分配</li>
 *   <li>正向耗尽后自动使用负向</li>
 *   <li>支持 ID 释放和区间合并</li>
 *   <li>线程安全（所有公共方法使用 synchronized）</li>
 *   <li>O(log n) 时间复杂度的分配和释放操作</li>
 * </ul>
 *
 * <h3>使用示例：</h3>
 * <pre>{@code
 * // 从已用 ID 集合初始化
 * Set<Integer> usedIds = new HashSet<>(Arrays.asList(1, 2, 5, 6, 7));
 * IdAllocator allocator = new IdAllocator(usedIds);
 *
 * // 分配 ID（返回 3，因为 1,2 已用，3 是第一个空洞）
 * int id1 = allocator.acquire(); // 3
 * int id2 = allocator.acquire(); // 4
 * int id3 = allocator.acquire(); // 8
 *
 * // 释放 ID
 * allocator.release(3);
 * allocator.release(4);
 * // 下次分配会优先使用刚释放的 ID
 * int id4 = allocator.acquire(); // 3
 * }</pre>
 */
public class IdAllocator {
    /**
     * 正向 ID 的起始值
     */
    private static final int NON_NEGATIVE_START = 0;

    /**
     * 正向的最大值
     */
    private static final int NON_NEGATIVE_MAX = Integer.MAX_VALUE;

    /**
     * 空洞区间映射表
     * <p>
     * Key: 区间起点（包含）
     * Value: 区间终点（包含）
     * </p>
     * <p>
     * 使用 TreeMap 保证区间按起点排序，便于：
     * <ul>
     *   <li>快速获取最小的空洞起点（firstKey）</li>
     *   <li>查找相邻区间进行合并（lowerEntry, higherEntry）</li>
     * </ul>
     * </p>
     */
    private final @NonNull TreeMap<Integer, Integer> holes;

    /**
     * 已使用的 ID 集合
     * <p>
     * 用于负数 ID 的分配和防止重复分配。
     * 同时用于初始化时计算空洞区间。
     * </p>
     */
    private final @NonNull Set<Integer> usedIds;

    /**
     * 负向 ID 游标
     * <p>
     * 当正向 ID 耗尽时，从此游标开始分配负向 ID。
     * 游标从最小值开始递减。
     * </p>
     */
    private int negativeCursor;

    /**
     * 创建一个空的 ID 分配器
     */
    public IdAllocator() {
        this(Collections.emptySet());
    }

    /**
     * 根据已用 ID 集合创建 ID 分配器
     *
     * @param usedIds 已使用的 ID 集合，不能为 null
     */
    public IdAllocator(@NonNull Set<Integer> usedIds) {
        this.holes = new TreeMap<>();
        this.usedIds = ConcurrentHashMap.newKeySet();

        // 初始化已用 ID 集合
        this.usedIds.addAll(usedIds);

        // 初始化空洞区间
        initHoles(usedIds);
    }

    /**
     * 根据已用 ID 初始化空洞区间
     * <p>
     * 算法说明：
     * <ol>
     *   <li>将所有已用 ID 排序</li>
     *   <li>遍历排序后的 ID，找出相邻 ID 之间的空洞</li>
     *   <li>空洞区间为 [prev + 1, current - 1]</li>
     *   <li>最后添加从最大已用 ID 到 Integer.MAX_VALUE 的区间</li>
     * </ol>
     * </p>
     *
     * @param usedIds 已使用的 ID 集合
     */
    private void initHoles(@NonNull Set<Integer> usedIds) {
        // 过滤并排序已用 ID
        val sortedPositiveIds = usedIds.stream()
            .sorted()
            .collect(Collectors.toList());

        if (sortedPositiveIds.isEmpty()) {
            // 没有已用的 ID，整个非负整数区间都是空洞
            holes.put(NON_NEGATIVE_START, NON_NEGATIVE_MAX);
            this.negativeCursor = -1;
            return;
        }

        val first = sortedPositiveIds.get(0);
        if (first > Integer.MIN_VALUE) {
            this.negativeCursor = first - 1;
        } else {
            this.negativeCursor = Integer.MIN_VALUE;
        }

        int prev = first; // 虚拟前驱，用于处理正向的情况

        for (val id : sortedPositiveIds) {
            if (id > prev + 1) {
                // 发现空洞：[prev + 1, id - 1]
                holes.put(prev + 1, id - 1);
            }
            prev = id;
        }

        // 添加最后一个区间：从最大已用 ID + 1 到 Integer.MAX_VALUE
        if (prev < NON_NEGATIVE_MAX) {
            holes.put(prev + 1, NON_NEGATIVE_MAX);
        }
    }

    /**
     * 获取一个可用的 ID
     * <p>
     * 分配策略：
     * <ol>
     *   <li>优先分配最小的正向空洞 ID</li>
     *   <li>如果正向耗尽，进行负向分配 ID</li>
     * </ol>
     * </p>
     *
     * @return 新分配的 ID
     */
    public synchronized int acquire() {
        // 尝试从空洞区间分配
        if (!holes.isEmpty()) {
            val first = holes.pollFirstEntry();
            val start = first.getKey();
            val end = first.getValue();

            // 分配起点 ID
            usedIds.add(start);

            // 如果区间还有剩余，放回剩余部分
            if (start < end) {
                holes.put(start + 1, end);
            }

            return start;
        }

        // 正向耗尽，进行负向分配
        while (usedIds.contains(negativeCursor)) {
            negativeCursor--;
        }

        val allocated = negativeCursor;
        usedIds.add(allocated);
        negativeCursor--;

        return allocated;
    }

    /**
     * 释放一个 ID，使其可被重新分配
     * <p>
     * 释放策略：
     * <ol>
     *   <li>从已用集合中移除该 ID</li>
     *   <li>尝试与相邻空洞区间合并</li>
     * </ol>
     * </p>
     *
     * @param id 要释放的 ID
     * @return true 如果释放成功，false 如果该 ID 未被使用
     */
    public synchronized boolean release(int id) {
        // 从已用集合中移除
        if (!usedIds.remove(id)) {
            return false;
        }

        // 尝试与相邻区间合并
        mergeHole(id);

        return true;
    }

    /**
     * 将释放的 ID 合并到空洞区间中
     * <p>
     * 合并逻辑：
     * <ol>
     *   <li>查找前一个区间（终点 = id - 1）</li>
     *   <li>查找后一个区间（起点 = id + 1）</li>
     *   <li>根据相邻区间情况进行合并</li>
     * </ol>
     * </p>
     *
     * @param id 释放的 ID
     */
    private void mergeHole(int id) {
        // 查找可能的前驱区间（终点 = id - 1）
        val lowerEntry = holes.lowerEntry(id);
        val hasLower = lowerEntry != null && lowerEntry.getValue() == id - 1;

        // 查找可能的后继区间（起点 = id + 1）
        val higherEntry = holes.higherEntry(id);
        val hasHigher = higherEntry != null && higherEntry.getKey() == id + 1;

        if (hasLower && hasHigher) {
            // 情况1：前后都有相邻区间，合并成一个大区间
            holes.remove(lowerEntry.getKey());
            holes.remove(higherEntry.getKey());
            holes.put(lowerEntry.getKey(), higherEntry.getValue());
        } else if (hasLower) {
            // 情况2：只有前驱区间相邻，扩展前驱区间的终点
            holes.put(lowerEntry.getKey(), id);
        } else if (hasHigher) {
            // 情况3：只有后继区间相邻，扩展后继区间的起点
            holes.remove(higherEntry.getKey());
            holes.put(id, higherEntry.getValue());
        } else {
            // 情况4：没有相邻区间，创建新的单点区间
            holes.put(id, id);
        }
    }

    /**
     * 检查指定 ID 是否已被使用
     *
     * @param id 要检查的 ID
     * @return true 如果 ID 已被使用
     */
    public synchronized boolean isUsed(int id) {
        return usedIds.contains(id);
    }

    /**
     * 获取当前已使用的 ID 数量
     *
     * @return 已使用 ID 的数量
     */
    public synchronized int getUsedCount() {
        return usedIds.size();
    }

    /**
     * 获取当前空洞区间的数量
     * <p>
     * 可用于监控碎片化程度。区间数量越多，碎片化越严重。
     * </p>
     *
     * @return 空洞区间的数量
     */
    public synchronized int getHoleCount() {
        return holes.size();
    }

    /**
     * 获取空洞区间的不可变包装
     * <p>
     * 返回的 Map 是当前空洞区间的不可变包装，无法修改。
     * </p>
     *
     * @return 空洞区间的不可变包装
     */
    public synchronized @NonNull NavigableMap<Integer, Integer> getHolesView() {
        return Collections.unmodifiableNavigableMap(holes);
    }

    /**
     * 获取空洞区间的快照
     * <p>
     * 返回的 Map 是当前空洞区间的副本，修改不会影响分配器状态。
     * </p>
     *
     * @return 空洞区间的不可修改副本
     */
    public synchronized @NonNull NavigableMap<Integer, Integer> getHolesSnapshot() {
        return Collections.unmodifiableNavigableMap(new TreeMap<>(holes));
    }

    /**
     * 获取已用 ID 的不可变包装
     *
     * @return 已用 ID 集合的不可变包装
     */
    public synchronized @NonNull Set<Integer> getUsedIdsView() {
        return Collections.unmodifiableSet(usedIds);
    }

    /**
     * 获取已用 ID 的快照
     *
     * @return 已用 ID 集合的不可修改副本
     */
    public synchronized @NonNull Set<Integer> getUsedIdsSnapshot() {
        return Collections.unmodifiableSet(new HashSet<>(usedIds));
    }

    /**
     * 获取下一个将被分配的 ID（不实际分配）
     * <p>
     * 如果没有可用的 ID（正负42亿个ID全部占用，你是个人物），直接报错。
     * </p>
     *
     * @return 下一个将被分配的 ID
     */
    public synchronized int peekNextId() {
        if (holes.isEmpty()) {
            while (usedIds.contains(negativeCursor)) {
                negativeCursor--;
            }
            return negativeCursor;
        }
        return holes.firstKey();
    }

    /**
     * 重置分配器状态
     * <p>
     * 清空所有已用 ID 和空洞区间，重新初始化。
     * </p>
     */
    public synchronized void reset() {
        usedIds.clear();
        holes.clear();
        negativeCursor = -1;
        holes.put(NON_NEGATIVE_START, NON_NEGATIVE_MAX);
    }

    /**
     * 根据新的已用 ID 集合重新初始化分配器
     *
     * @param newUsedIds 新的已用 ID 集合
     */
    public synchronized void reinitialize(@NonNull Set<Integer> newUsedIds) {
        usedIds.clear();
        holes.clear();

        usedIds.addAll(newUsedIds);
        initHoles(newUsedIds);
    }

    @Override
    public synchronized @NonNull String toString() {
        return "IdAllocator{" +
            "usedCount=" + usedIds.size() +
            ", holeCount=" + holes.size() +
            ", negativeCursor=" + negativeCursor +
            ", nextPositiveId=" + (holes.isEmpty() ? "N/A" : holes.firstKey()) +
            '}';
    }
}
