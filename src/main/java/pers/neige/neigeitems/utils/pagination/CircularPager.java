package pers.neige.neigeitems.utils.pagination;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.utils.pagination.impl.circular.ListCircularPager;
import pers.neige.neigeitems.utils.pagination.impl.circular.MutableNavMapCircularPager;
import pers.neige.neigeitems.utils.pagination.impl.circular.MutableNavSetCircularPager;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * 循环分页工具
 */
public abstract class CircularPager<T> {
    protected final int pageSize;
    protected final @Nullable Predicate<T> filter;

    protected CircularPager(int pageSize, @Nullable Predicate<T> filter) {
        this.pageSize = Math.max(pageSize, 1);
        this.filter = filter;
    }

    /**
     * 根据一个不可变 Iterable 创建循环分页工具.<br>
     * 元素顺序为迭代器遍历顺序.<br>
     * 此后对该 Iterable 的任何修改将无法反应到已创建的循环分页工具中.<br>
     * pageSize 小于等于 0 时将自动变为 1.
     *
     * @param handle   待分页 Iterable
     * @param pageSize 页大小
     * @param filter   元素过滤器
     * @return 分页工具
     */
    public static <T> @NotNull CircularPager<T> fromImmutableIterable(@NotNull Iterable<T> handle, int pageSize, @Nullable Predicate<T> filter) {
        List<T> handleClone = StreamSupport.stream(handle.spliterator(), false)
                .filter(t -> filter == null || filter.test(t))
                .collect(Collectors.toList());
        return new ListCircularPager<>(Collections.unmodifiableList(handleClone), pageSize, null);
    }

    /**
     * 根据一个不可变数组创建循环分页工具.<br>
     * 元素顺序为数组顺序.<br>
     * 此后对该数组的任何修改将无法反应到已创建的循环分页工具中.<br>
     * pageSize 小于等于 0 时将自动变为 1.
     *
     * @param handle   待分页数组
     * @param pageSize 页大小
     * @param filter   元素过滤器
     * @return 分页工具
     */
    public static <T> @NotNull CircularPager<T> fromImmutableArray(T @NotNull [] handle, int pageSize, @Nullable Predicate<T> filter) {
        return fromImmutableIterable(Arrays.asList(handle), pageSize, filter);
    }

    /**
     * 根据一个可变 List 创建循环分页工具.<br>
     * 元素顺序为迭代器遍历顺序.<br>
     * 此后对该 List 的任何修改将即时反应于已创建的循环分页工具中.<br>
     * pageSize 小于等于 0 时将自动变为 1.
     *
     * @param handle   待分页 List
     * @param pageSize 页大小
     * @param filter   元素过滤器
     * @return 分页工具
     */
    public static <T> @NotNull CircularPager<T> fromMutableList(@NotNull List<T> handle, int pageSize, @Nullable Predicate<T> filter) {
        return new ListCircularPager<>(handle, pageSize, filter);
    }

    /**
     * 根据一个可变 NavigableSet 创建循环分页工具.<br>
     * 元素顺序为迭代器遍历顺序.<br>
     * 此后对该 NavigableSet 的任何修改将即时反应于已创建的循环分页工具中.<br>
     * pageSize 小于等于 0 时将自动变为 1.
     *
     * @param handle   待分页 Set
     * @param pageSize 页大小
     * @param filter   元素过滤器
     * @return 分页工具
     */
    public static <T> @NotNull CircularPager<T> fromMutableNavSet(@NotNull NavigableSet<T> handle, int pageSize, @Nullable Predicate<T> filter) {
        return new MutableNavSetCircularPager<>(handle, pageSize, filter);
    }

    /**
     * 根据一个可变 NavigableMap 创建循环分页工具.<br>
     * 元素顺序为迭代器遍历顺序.<br>
     * 此后对该 NavigableMap 的任何修改将即时反应于已创建的循环分页工具中.<br>
     * pageSize 小于等于 0 时将自动变为 1.
     *
     * @param handle   待分页 Map
     * @param pageSize 页大小
     * @param filter   元素过滤器
     * @return 分页工具
     */
    public static <K, V> @NotNull CircularPager<Map.Entry<K, V>> fromMutableNavMap(@NotNull NavigableMap<K, V> handle, int pageSize, @Nullable Predicate<Map.Entry<K, V>> filter) {
        return new MutableNavMapCircularPager<>(handle, pageSize, filter);
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
