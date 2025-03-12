package pers.neige.neigeitems.utils.pagination.impl;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.utils.pagination.PaginationTool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ListPaginationTool<T> extends PaginationTool<T> {
    /**
     * 数据副本，初始化后不变
     */
    private final @NotNull List<T> list;
    /**
     * 总页数，初始化后不变
     */
    private final int totalPages;
    /**
     * 当前页码（从1开始）
     */
    private final @NotNull AtomicInteger currentPage;

    public ListPaginationTool(@NotNull List<T> list, int pageSize) {
        super(pageSize);
        this.list = new ArrayList<>(list);
        this.totalPages = calculateTotalPages();
        // 初始化当前页（总页数为0时设为0，否则从1开始）
        this.currentPage = new AtomicInteger(totalPages == 0 ? 0 : 1);
    }

    /**
     * 计算总页数
     */
    private int calculateTotalPages() {
        int size = list.size();
        return size == 0 ? 0 : (size + pageSize - 1) / pageSize;
    }

    public boolean nextPage() {
        if (totalPages <= 1) return false;
        int current;
        do {
            current = currentPage.get();
            if (current >= totalPages) {
                return false; // 已到最后一页，无法翻页
            }
        } while (!currentPage.compareAndSet(current, current + 1)); // CAS更新
        return true; // 翻页成功
    }

    public boolean prevPage() {
        if (totalPages <= 1) return false;
        int current;
        do {
            current = currentPage.get();
            if (current <= 1) {
                return false; // 已到第一页，无法翻页
            }
        } while (!currentPage.compareAndSet(current, current - 1)); // CAS更新
        return true; // 翻页成功
    }

    public boolean goToPage(int page) {
        if (page < 1 || page > getTotalPages()) {
            return false;
        }
        currentPage.set(page); // 原子性设置
        return true;
    }

    public int getCurrentPage() {
        return currentPage.get();
    }

    public @NotNull List<T> getCurrentPageElements() {
        int current = currentPage.get();
        if (current == 0) {
            return Collections.emptyList();
        }
        int startIndex = (current - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, list.size());
        if (startIndex >= endIndex) {
            return Collections.emptyList();
        }
        return list.subList(startIndex, endIndex);
    }

    public boolean hasNextPage() {
        return currentPage.get() < totalPages;
    }

    public boolean hasPrevPage() {
        return currentPage.get() > 1;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalElements() {
        return list.size();
    }

    public static class Unsafe {
        public static <T> @NotNull List<T> getHandle(ListPaginationTool<T> tool) {
            return tool.list;
        }
    }
}
