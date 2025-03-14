package pers.neige.neigeitems.utils.pagination.impl;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.utils.pagination.Pager;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MutableListPager<T> extends Pager<T> {
    /**
     * 可变数据
     */
    private final @NotNull List<T> handle;
    /**
     * 当前页码（从1开始）
     */
    private final @NotNull AtomicInteger currentPage;

    public MutableListPager(@NotNull List<T> handle, int pageSize) {
        super(pageSize);
        this.handle = handle;
        // 初始化当前页（总页数为0时设为0，否则从1开始）
        this.currentPage = new AtomicInteger(getTotalPages() == 0 ? 0 : 1);
    }

    public @NotNull List<T> getHandle() {
        return handle;
    }

    public boolean nextPage() {
        if (getTotalPages() <= 1) return false;
        int current;
        do {
            current = getCurrentPage();
            if (current >= getTotalPages()) {
                return false;
            }
        } while (!currentPage.compareAndSet(current, current + 1));
        return true;
    }

    public boolean prevPage() {
        if (getTotalPages() <= 1) return false;
        int current;
        do {
            current = getCurrentPage();
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
        int page;
        int maybeReplacement;
        do {
            page = currentPage.get();
            if (page == 0 && !handle.isEmpty()) {
                maybeReplacement = 1;
            } else {
                maybeReplacement = Math.min(getTotalPages(), page);
            }
        } while (page != maybeReplacement && !currentPage.compareAndSet(page, maybeReplacement));
        return maybeReplacement;
    }

    public @NotNull List<T> getCurrentPageElements() {
        int current = getCurrentPage();
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
        return getCurrentPage() < getTotalPages();
    }

    public boolean hasPrevPage() {
        return getCurrentPage() > 1;
    }

    public int getTotalElements() {
        return handle.size();
    }
}
