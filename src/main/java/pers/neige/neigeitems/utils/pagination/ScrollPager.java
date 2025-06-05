package pers.neige.neigeitems.utils.pagination;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.utils.pagination.impl.scroll.ListScrollPager;
import pers.neige.neigeitems.utils.pagination.impl.scroll.MutableNavMapScrollPager;
import pers.neige.neigeitems.utils.pagination.impl.scroll.MutableNavSetScrollPager;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * 滚动分页工具, 非并发安全(曾尝试进行并发安全处理, 但失败了)
 */
public abstract class ScrollPager<T> {
    protected final @NonNull PagerInfo pagerInfo;
    protected final @Nullable Predicate<T> filter;

    protected ScrollPager(int pageSize, @Nullable Predicate<T> filter) {
        this.pagerInfo = new PagerInfo(Math.max(1, pageSize));
        this.filter = filter;
    }

    /**
     * 根据一个不可变 Iterable 创建滚动分页工具.<br>
     * 元素顺序为迭代器遍历顺序.<br>
     * 此后对该 Iterable 的任何修改将无法反应到已创建的滚动分页工具中.<br>
     * pageSize 小于等于 0 时将自动变为 1.
     *
     * @param handle   待分页 Iterable
     * @param pageSize 页大小
     * @param filter   元素过滤器
     * @return 分页工具
     */
    public static <T> @NonNull ScrollPager<T> fromImmutableIterable(@NonNull Iterable<T> handle, int pageSize, @Nullable Predicate<T> filter) {
        val handleClone = StreamSupport.stream(handle.spliterator(), false)
                .filter(t -> filter == null || filter.test(t))
                .collect(Collectors.toList());
        return new ListScrollPager<>(Collections.unmodifiableList(handleClone), pageSize, null);
    }

    /**
     * 根据一个不可变数组创建滚动分页工具.<br>
     * 元素顺序为数组顺序.<br>
     * 此后对该数组的任何修改将无法反应到已创建的滚动分页工具中.<br>
     * pageSize 小于等于 0 时将自动变为 1.
     *
     * @param handle   待分页数组
     * @param pageSize 页大小
     * @param filter   元素过滤器
     * @return 分页工具
     */
    public static <T> @NonNull ScrollPager<T> fromImmutableArray(T @NonNull [] handle, int pageSize, @Nullable Predicate<T> filter) {
        return fromImmutableIterable(Arrays.asList(handle), pageSize, filter);
    }

    /**
     * 根据一个可变 List 创建滚动分页工具.<br>
     * 元素顺序为迭代器遍历顺序.<br>
     * 此后对该 List 的任何修改将即时反应于已创建的滚动分页工具中.<br>
     * pageSize 小于等于 0 时将自动变为 1.
     *
     * @param handle   待分页 List
     * @param pageSize 页大小
     * @param filter   元素过滤器
     * @return 分页工具
     */
    public static <T> @NonNull ScrollPager<T> fromMutableList(@NonNull List<T> handle, int pageSize, @Nullable Predicate<T> filter) {
        return new ListScrollPager<>(handle, pageSize, filter);
    }

    /**
     * 根据一个可变 NavigableSet 创建滚动分页工具.<br>
     * 元素顺序为迭代器遍历顺序.<br>
     * 此后对该 NavigableSet 的任何修改将即时反应于已创建的滚动分页工具中.<br>
     * pageSize 小于等于 0 时将自动变为 1.
     *
     * @param handle   待分页 Set
     * @param pageSize 页大小
     * @param filter   元素过滤器
     * @return 分页工具
     */
    public static <T> @NonNull ScrollPager<T> fromMutableNavSet(@NonNull NavigableSet<T> handle, int pageSize, @Nullable Predicate<T> filter) {
        return new MutableNavSetScrollPager<>(handle, pageSize, filter);
    }

    /**
     * 根据一个可变 NavigableMap 创建滚动分页工具.<br>
     * 元素顺序为迭代器遍历顺序.<br>
     * 此后对该 NavigableMap 的任何修改将即时反应于已创建的滚动分页工具中.<br>
     * pageSize 小于等于 0 时将自动变为 1.
     *
     * @param handle   待分页 Map
     * @param pageSize 页大小
     * @param filter   元素过滤器
     * @return 分页工具
     */
    public static <K, V> @NonNull ScrollPager<Map.Entry<K, V>> fromMutableNavMap(@NonNull NavigableMap<K, V> handle, int pageSize, @Nullable Predicate<Map.Entry<K, V>> filter) {
        return new MutableNavMapScrollPager<>(handle, pageSize, filter);
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
     * 将偏移调整至最后一位
     */
    public abstract void toFinalOffset();

    /**
     * 移动偏移
     *
     * @param delta 偏移移动量
     */
    public abstract void moveOffset(int delta);

    /**
     * 根据过滤器移动偏移, 从当前偏移开始向前推进, 直至满足过滤器条件
     *
     * @param delta 偏移移动量
     */
    public abstract void moveOffsetByFilter(int delta);

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

    /**
     * 跳转到下一页（如果存在）
     *
     * @return 是否存在下一页
     */
    public abstract boolean nextPage();

    /**
     * 跳转到上一页（如果存在）
     *
     * @return 是否存在上一页
     */
    public abstract boolean prevPage();

    /**
     * 检查是否存在下一页
     */
    public abstract boolean hasNextPage();

    /**
     * 检查是否存在上一页
     */
    public abstract boolean hasPrevPage();
}
