package pers.neige.neigeitems.utils.pagination.impl;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.utils.pagination.PaginationTool;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ImmutableListPaginationTool<T> extends PaginationTool<T> {
    /**
     * 数据副本，初始化后不变
     */
    private final @NotNull List<T> handle;
    /**
     * 总页数，初始化后不变
     */
    private final int totalPages;
    /**
     * 当前页码（从1开始）
     */
    private final @NotNull AtomicInteger currentPage;

    public ImmutableListPaginationTool(@NotNull List<T> handle, int pageSize) {
        super(pageSize);
        this.handle = handle;
        this.totalPages = super.getTotalPages();
        // 初始化当前页（总页数为0时设为0，否则从1开始）
        this.currentPage = new AtomicInteger(totalPages == 0 ? 0 : 1);
    }

    public @NotNull List<T> getHandle() {
        return handle;
    }

    public boolean nextPage() {
        if (totalPages <= 1) return false;
        int current;
        do {
            current = currentPage.get();
            if (current >= totalPages) {
                return false;
            }
        } while (!currentPage.compareAndSet(current, current + 1));
        return true;
    }

    public boolean prevPage() {
        if (totalPages <= 1) return false;
        int current;
        do {
            current = currentPage.get();
            if (current <= 1) {
                return false;
            }
        } while (!currentPage.compareAndSet(current, current - 1));
        return true;
    }

    public boolean goToPage(int page) {
        if (page < 1 || page > getTotalPages()) {
            return false;
        }
        currentPage.set(page);
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
        int startIndex = (current - 1) * getPageSize();
        int endIndex = Math.min(startIndex + getPageSize(), handle.size());
        if (startIndex >= endIndex) {
            return Collections.emptyList();
        }
        return handle.subList(startIndex, endIndex);
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
        return handle.size();
    }
}
