package pers.neige.neigeitems.utils.pagination;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class PaginationTool<T> {
    /**
     * 每页元素数，初始化后不变
     */
    protected final int pageSize;

    public PaginationTool(int pageSize) {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("页大小必须大于0");
        }
        this.pageSize = pageSize;
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
    public abstract int getTotalPages();

    /**
     * 获取总元素数
     */
    public abstract int getTotalElements();
}
