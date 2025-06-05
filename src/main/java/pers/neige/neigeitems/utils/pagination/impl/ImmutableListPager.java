package pers.neige.neigeitems.utils.pagination.impl;

import lombok.NonNull;
import lombok.val;
import pers.neige.neigeitems.utils.pagination.Pager;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ImmutableListPager<T> extends Pager<T> {
    /**
     * 数据副本，初始化后不变
     */
    private final @NonNull List<T> handle;
    /**
     * 总页数，初始化后不变
     */
    private final int totalPages;
    /**
     * 当前页码（从1开始）
     */
    private final @NonNull AtomicInteger currentPage;

    public ImmutableListPager(@NonNull List<T> handle, int pageSize) {
        super(pageSize);
        this.handle = handle;
        this.totalPages = super.getTotalPages();
        // 初始化当前页（总页数为0时设为0，否则从1开始）
        this.currentPage = new AtomicInteger(totalPages == 0 ? 0 : 1);
    }

    public @NonNull List<T> getHandle() {
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

    public @NonNull List<T> getCurrentPageElements() {
        val current = currentPage.get();
        if (current == 0) {
            return Collections.emptyList();
        }
        val startIndex = (current - 1) * getPageSize();
        val endIndex = Math.min(startIndex + getPageSize(), handle.size());
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
