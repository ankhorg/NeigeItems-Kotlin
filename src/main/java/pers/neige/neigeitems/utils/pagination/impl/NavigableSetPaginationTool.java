package pers.neige.neigeitems.utils.pagination.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.utils.pagination.PageCursor;
import pers.neige.neigeitems.utils.pagination.PaginationTool;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class NavigableSetPaginationTool<T> extends PaginationTool<T> {
    /**
     * 数据副本，初始化后不变
     */
    private final @NotNull NavigableSet<T> navigableSet;
    /**
     * 总页数，初始化后不变
     */
    private final int totalPages;
    /**
     * 当前页游标
     */
    private final @NotNull AtomicReference<PageCursor<T>> cursorRef;

    public NavigableSetPaginationTool(@NotNull NavigableSet<T> set, int pageSize) {
        super(pageSize);
        this.navigableSet = new TreeSet<>(set);
        this.totalPages = calculateTotalPages();
        this.cursorRef = new AtomicReference<>(initCursor());
    }

    /**
     * 计算总页数（O(n)操作，初始化时执行一次）
     */
    private int calculateTotalPages() {
        int size = navigableSet.size();
        return size == 0 ? 0 : (size + pageSize - 1) / pageSize;
    }

    /**
     * 初始化游标位置（第一页起始键）
     */
    private @NotNull PageCursor<T> initCursor() {
        if (navigableSet.isEmpty()) return PageCursor.empty();
        return new PageCursor<>(navigableSet.first(), 1);
    }

    public boolean nextPage() {
        PageCursor<T> prev;
        PageCursor<T> next;
        do {
            prev = cursorRef.get();
            if (prev.currentPage >= totalPages) return false;

            T nextStart = findNextStart(prev.startElement);
            if (nextStart == null) return false;

            int newPage = prev.currentPage + 1;
            next = new PageCursor<>(nextStart, newPage);
        } while (!cursorRef.compareAndSet(prev, next));
        return true;
    }

    public boolean prevPage() {
        PageCursor<T> prev;
        PageCursor<T> next;
        do {
            prev = cursorRef.get();
            if (prev.currentPage <= 1) return false;

            T prevStart = findPrevStart(prev.startElement);
            if (prevStart == null) return false;

            int newPage = prev.currentPage - 1;
            next = new PageCursor<>(prevStart, newPage);
        } while (!cursorRef.compareAndSet(prev, next));
        return true;
    }

    public boolean goToPage(int page) {
        if (page < 1 || page > totalPages) return false;

        PageCursor<T> target = calculatePageStart(page);
        if (target == null) return false;

        cursorRef.set(target);
        return true;
    }

    public int getCurrentPage() {
        return cursorRef.get().currentPage;
    }

    public @NotNull List<T> getCurrentPageElements() {
        PageCursor<T> cursor = cursorRef.get();
        if (cursor.isEmpty()) return Collections.emptyList();

        List<T> pageElements = new ArrayList<>(pageSize);
        Iterator<T> it = navigableSet.tailSet(cursor.startElement, true).iterator();

        int count = 0;
        while (it.hasNext() && count < pageSize) {
            pageElements.add(it.next());
            count++;
        }
        return pageElements;
    }

    public boolean hasNextPage() {
        return cursorRef.get().currentPage < totalPages;
    }

    public boolean hasPrevPage() {
        return cursorRef.get().currentPage > 1;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalElements() {
        return navigableSet.size();
    }

    // ---------------------------- 内部辅助方法 ----------------------------
    private T findNextStart(T currentStart) {
        Iterator<T> it = navigableSet.tailSet(currentStart, false).iterator();
        for (int i = 0; i < pageSize - 1 && it.hasNext(); i++) {
            it.next();
        }
        return it.hasNext() ? it.next() : null;
    }

    private T findPrevStart(T currentStart) {
        NavigableSet<T> headSet = navigableSet.headSet(currentStart, false);
        if (headSet.isEmpty()) return null;

        Iterator<T> it = headSet.descendingIterator();
        int steps = (pageSize - 1) % pageSize; // 计算上一页起始偏移
        for (int i = 0; i < steps && it.hasNext(); i++) {
            it.next();
        }
        return it.hasNext() ? it.next() : navigableSet.first();
    }

    private @Nullable PageCursor<T> calculatePageStart(int targetPage) {
        if (targetPage == 1) return new PageCursor<>(navigableSet.first(), 1);

        Iterator<T> it = navigableSet.iterator();
        int skip = (targetPage - 1) * pageSize;

        for (int i = 0; i < skip && it.hasNext(); i++) {
            it.next();
        }
        return it.hasNext() ? new PageCursor<>(it.next(), targetPage) : null;
    }

    public static class Unsafe {
        public static <T> @NotNull NavigableSet<T> getHandle(NavigableSetPaginationTool<T> tool) {
            return tool.navigableSet;
        }
    }
}
