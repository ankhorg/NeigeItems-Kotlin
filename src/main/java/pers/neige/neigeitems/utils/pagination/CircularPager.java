package pers.neige.neigeitems.utils.pagination;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.utils.pagination.impl.circular.ImmutableListCircularPager;
import pers.neige.neigeitems.utils.pagination.impl.circular.MutableListCircularPager;
import pers.neige.neigeitems.utils.pagination.impl.circular.MutableNavMapCircularPager;
import pers.neige.neigeitems.utils.pagination.impl.circular.MutableNavSetCircularPager;

import java.util.*;

public abstract class CircularPager<T> {
    protected final int pageSize;

    protected CircularPager(int pageSize) {
        this.pageSize = Math.max(pageSize, 1);
    }

    /**
     * 根据一个不可变 List 创建循环分页工具.<br>
     * 元素顺序为迭代器遍历顺序.<br>
     * 如果 clone 项为 true, 此后对该 List 的任何修改将无法反应到已创建的循环分页工具中.<br>
     * 如果 clone 项为 false, 此后对该 List 的任何修改将导致不可预知的错误.<br>
     * pageSize 小于等于 0 时将自动变为 1.
     *
     * @param handle   待分页 List
     * @param pageSize 页大小
     * @param clone    是否对传入的 List 进行复制
     * @return 分页工具
     */
    public static <T> @NotNull CircularPager<T> fromImmutableList(@NotNull List<T> handle, int pageSize, boolean clone) {
        return new ImmutableListCircularPager<>(clone ? Collections.unmodifiableList(new ArrayList<>(handle)) : handle, pageSize);
    }

    /**
     * 根据一个不可变数组创建循环分页工具.<br>
     * 元素顺序为数组顺序.<br>
     * 此后对该数组的任何修改将无法反应到已创建的循环分页工具中.<br>
     * pageSize 小于等于 0 时将自动变为 1.
     *
     * @param handle   待分页数组
     * @param pageSize 页大小
     * @return 分页工具
     */
    public static <T> @NotNull CircularPager<T> fromImmutableArray(T @NotNull [] handle, int pageSize) {
        return fromImmutableList(Arrays.asList(handle), pageSize, true);
    }

    /**
     * 根据一个不可变 Set 创建循环分页工具.<br>
     * 元素顺序为迭代器遍历顺序.<br>
     * 此后对该 Set 的任何修改将无法反应到已创建的循环分页工具中.<br>
     * pageSize 小于等于 0 时将自动变为 1.
     *
     * @param handle   待分页 Set
     * @param pageSize 页大小
     * @return 分页工具
     */
    public static <T> @NotNull CircularPager<T> fromImmutableSet(@NotNull Set<T> handle, int pageSize) {
        return fromImmutableList(Collections.unmodifiableList(new ArrayList<>(handle)), pageSize, false);
    }

    /**
     * 根据一个不可变 Map 创建循环分页工具.<br>
     * 元素顺序为迭代器遍历顺序.<br>
     * 此后对该 Map 的任何修改将无法反应到已创建的循环分页工具中.<br>
     * pageSize 小于等于 0 时将自动变为 1.
     *
     * @param handle   待分页 Map
     * @param pageSize 页大小
     * @return 分页工具
     */
    public static <K, V> @NotNull CircularPager<Map.Entry<K, V>> fromImmutableMap(@NotNull Map<K, V> handle, int pageSize) {
        return fromImmutableList(Collections.unmodifiableList(new ArrayList<>(handle.entrySet())), pageSize, false);
    }

    /**
     * 根据一个可变 List 创建循环分页工具.<br>
     * 元素顺序为迭代器遍历顺序.<br>
     * 此后对该 List 的任何修改将即时反应于已创建的循环分页工具中.<br>
     * pageSize 小于等于 0 时将自动变为 1.
     *
     * @param handle   待分页 List
     * @param pageSize 页大小
     * @return 分页工具
     */
    public static <T> @NotNull CircularPager<T> fromMutableList(@NotNull List<T> handle, int pageSize) {
        return new MutableListCircularPager<>(handle, pageSize);
    }

    /**
     * 根据一个可变 NavigableSet 创建循环分页工具.<br>
     * 元素顺序为迭代器遍历顺序.<br>
     * 此后对该 NavigableSet 的任何修改将即时反应于已创建的循环分页工具中.<br>
     * pageSize 小于等于 0 时将自动变为 1.
     *
     * @param handle   待分页 Set
     * @param pageSize 页大小
     * @return 分页工具
     */
    public static <T> @NotNull CircularPager<T> fromMutableNavSet(@NotNull NavigableSet<T> handle, int pageSize) {
        return new MutableNavSetCircularPager<>(handle, pageSize);
    }

    /**
     * 根据一个可变 NavigableMap 创建循环分页工具.<br>
     * 元素顺序为迭代器遍历顺序.<br>
     * 此后对该 NavigableMap 的任何修改将即时反应于已创建的循环分页工具中.<br>
     * pageSize 小于等于 0 时将自动变为 1.
     *
     * @param handle   待分页 Map
     * @param pageSize 页大小
     * @return 分页工具
     */
    public static <K, V> @NotNull CircularPager<Map.Entry<K, V>> fromMutableNavMap(@NotNull NavigableMap<K, V> handle, int pageSize) {
        return new MutableNavMapCircularPager<>(handle, pageSize);
    }

    /**
     * 获取分页大小
     *
     * @return 分页大小
     */
    public final int getPageSize() {
        return pageSize;
    }

    /**
     * 重置偏移
     */
    public abstract void resetOffset();

    /**
     * 移动偏移
     *
     * @param delta 偏移移动量
     */
    public abstract void moveOffset(int delta);

    /**
     * 获取当前页的元素列表
     */
    public abstract @NotNull List<T> getCurrentPageElements();

    /**
     * 获取总元素数
     */
    public abstract int getTotalElements();
}
