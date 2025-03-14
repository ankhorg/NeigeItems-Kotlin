package pers.neige.neigeitems.utils.pagination;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.utils.pagination.impl.ImmutableListPager;
import pers.neige.neigeitems.utils.pagination.impl.MutableListPager;
import pers.neige.neigeitems.utils.pagination.impl.MutableNavMapPager;
import pers.neige.neigeitems.utils.pagination.impl.MutableNavSetPager;

import java.util.*;

public abstract class Pager<T> {
    /**
     * 页面信息
     */
    protected final PagerInfo pagerInfo;

    protected Pager(int pageSize) {
        this.pagerInfo = new PagerInfo(Math.max(1, pageSize));
    }

    /**
     * 根据一个不可变 List 创建分页工具.<br>
     * 元素顺序为迭代器遍历顺序.<br>
     * 如果 clone 项为 true, 此后对该 List 的任何修改将无法反应到已创建的分页工具中.<br>
     * 如果 clone 项为 false, 此后对该 List 的任何修改将导致不可预知的错误.<br>
     * pageSize 小于等于 0 时将自动变为 1.
     *
     * @param handle   待分页 List
     * @param pageSize 页大小
     * @param clone    是否对传入的 List 进行复制
     * @return 分页工具
     */
    public static <T> @NotNull Pager<T> fromImmutableList(@NotNull List<T> handle, int pageSize, boolean clone) {
        return new ImmutableListPager<>(clone ? Collections.unmodifiableList(new ArrayList<>(handle)) : handle, pageSize);
    }

    /**
     * 根据一个不可变数组创建分页工具.<br>
     * 元素顺序为数组顺序.<br>
     * 此后对该数组的任何修改将无法反应到已创建的分页工具中.<br>
     * pageSize 小于等于 0 时将自动变为 1.
     *
     * @param handle   待分页数组
     * @param pageSize 页大小
     * @return 分页工具
     */
    public static <T> @NotNull Pager<T> fromImmutableArray(T @NotNull [] handle, int pageSize) {
        return fromImmutableList(Arrays.asList(handle), pageSize, true);
    }

    /**
     * 根据一个不可变 Set 创建分页工具.<br>
     * 元素顺序为迭代器遍历顺序.<br>
     * 此后对该 Set 的任何修改将无法反应到已创建的分页工具中.<br>
     * pageSize 小于等于 0 时将自动变为 1.
     *
     * @param handle   待分页 Set
     * @param pageSize 页大小
     * @return 分页工具
     */
    public static <T> @NotNull Pager<T> fromImmutableSet(@NotNull Set<T> handle, int pageSize) {
        return fromImmutableList(Collections.unmodifiableList(new ArrayList<>(handle)), pageSize, false);
    }

    /**
     * 根据一个不可变 Map 创建分页工具.<br>
     * 元素顺序为迭代器遍历顺序.<br>
     * 此后对该 Map 的任何修改将无法反应到已创建的分页工具中.<br>
     * pageSize 小于等于 0 时将自动变为 1.
     *
     * @param handle   待分页 Map
     * @param pageSize 页大小
     * @return 分页工具
     */
    public static <K, V> @NotNull Pager<Map.Entry<K, V>> fromImmutableMap(@NotNull Map<K, V> handle, int pageSize) {
        return fromImmutableList(Collections.unmodifiableList(new ArrayList<>(handle.entrySet())), pageSize, false);
    }

    /**
     * 根据一个可变 List 创建分页工具.<br>
     * 元素顺序为迭代器遍历顺序.<br>
     * 此后对该 List 的任何修改将即时反应于已创建的分页工具中.<br>
     * pageSize 小于等于 0 时将自动变为 1.
     *
     * @param handle   待分页 List
     * @param pageSize 页大小
     * @return 分页工具
     */
    public static <T> @NotNull Pager<T> fromMutableList(@NotNull List<T> handle, int pageSize) {
        return new MutableListPager<>(handle, pageSize);
    }

    /**
     * 根据一个可变 NavigableSet 创建分页工具.<br>
     * 元素顺序为迭代器遍历顺序.<br>
     * 此后对该 NavigableSet 的任何修改将即时反应于已创建的分页工具中.<br>
     * pageSize 小于等于 0 时将自动变为 1.
     *
     * @param handle   待分页 Set
     * @param pageSize 页大小
     * @return 分页工具
     */
    public static <T> @NotNull Pager<T> fromMutableNavSet(@NotNull NavigableSet<T> handle, int pageSize) {
        return new MutableNavSetPager<>(handle, pageSize);
    }

    /**
     * 根据一个可变 NavigableMap 创建分页工具.<br>
     * 元素顺序为迭代器遍历顺序.<br>
     * 此后对该 NavigableMap 的任何修改将即时反应于已创建的分页工具中.<br>
     * pageSize 小于等于 0 时将自动变为 1.
     *
     * @param handle   待分页 Map
     * @param pageSize 页大小
     * @return 分页工具
     */
    public static <K, V> @NotNull Pager<Map.Entry<K, V>> fromMutableNavMap(@NotNull NavigableMap<K, V> handle, int pageSize) {
        return new MutableNavMapPager<>(handle, pageSize);
    }

    /**
     * 获取分页大小
     *
     * @return 分页大小
     */
    public int getPageSize() {
        return this.pagerInfo.getPageSize();
    }

    /**
     * 跳转到第一页（如果存在）
     *
     * @return 是否存在任意元素
     */
    public boolean firstPage() {
        return goToPage(1);
    }

    /**
     * 跳转到最后一页（如果存在）
     *
     * @return 是否存在任意元素
     */
    public boolean lastPage() {
        return goToPage(getTotalPages());
    }

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
     * 跳转到指定页
     *
     * @param page 目标页码（从1开始）
     * @return 页码是否有效
     */
    public abstract boolean goToPage(int page);

    /**
     * 获取当前页码（从1开始）
     */
    public abstract int getCurrentPage();

    /**
     * 获取当前页的元素列表
     */
    public abstract @NotNull List<T> getCurrentPageElements();

    /**
     * 检查是否存在下一页
     */
    public abstract boolean hasNextPage();

    /**
     * 检查是否存在上一页
     */
    public abstract boolean hasPrevPage();

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
