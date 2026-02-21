package pers.neige.neigeitems.utils.pagination;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.utils.pagination.impl.circular.ListCircularPager;
import pers.neige.neigeitems.utils.pagination.impl.circular.MutableNavMapCircularPager;
import pers.neige.neigeitems.utils.pagination.impl.circular.MutableNavSetCircularPager;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * 循环分页工具, 非并发安全(曾尝试进行并发安全处理, 但失败了)
 */
public abstract class CircularPager<T> {
    protected final @NonNull PagerInfo pagerInfo;
    protected final @Nullable Predicate<T> filter;

    protected CircularPager(int pageSize, @Nullable Predicate<T> filter) {
        this.pagerInfo = new PagerInfo(Math.max(1, pageSize));
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
    public static <T> @NonNull CircularPager<T> fromImmutableIterable(@NonNull Iterable<T> handle, int pageSize, @Nullable Predicate<T> filter) {
        val handleClone = StreamSupport.stream(handle.spliterator(), false)
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
    public static <T> @NonNull CircularPager<T> fromImmutableArray(T @NonNull [] handle, int pageSize, @Nullable Predicate<T> filter) {
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
    public static <T> @NonNull CircularPager<T> fromMutableList(@NonNull List<T> handle, int pageSize, @Nullable Predicate<T> filter) {
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
    public static <T> @NonNull CircularPager<T> fromMutableNavSet(@NonNull NavigableSet<T> handle, int pageSize, @Nullable Predicate<T> filter) {
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
    public static <K, V> @NonNull CircularPager<Map.Entry<K, V>> fromMutableNavMap(@NonNull NavigableMap<K, V> handle, int pageSize, @Nullable Predicate<Map.Entry<K, V>> filter) {
        return new MutableNavMapCircularPager<>(handle, pageSize, filter);
    }

    /**
     * 获取分页大小
     *
     * @return 分页大小
     */
    public final int getPageSize() {
        return pagerInfo.getPageSize();
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
    public abstract @NonNull List<T> getCurrentPageElements();

    /**
     * 获取总页数
     */
    public int getTotalPages() {
        return this.pagerInfo.getTotalPages(getTotalElements());
    }

    /**
     * 获取总元素数
     */
    public abstract int getTotalElements();
}
