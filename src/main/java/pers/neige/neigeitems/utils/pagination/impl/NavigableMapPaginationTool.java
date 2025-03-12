package pers.neige.neigeitems.utils.pagination.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.utils.pagination.PageCursor;
import pers.neige.neigeitems.utils.pagination.PaginationTool;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;

public class NavigableMapPaginationTool<K, V> extends PaginationTool<Entry<K, V>> {
    /**
     * 数据副本，初始化后不变
     */
    private final @NotNull NavigableMap<K, V> navigableMap;
    /**
     * 总页数，初始化后不变
     */
    private final int totalPages;
    /**
     * 当前页游标
     */
    private final @NotNull AtomicReference<PageCursor<K>> cursorRef;

    public NavigableMapPaginationTool(@NotNull NavigableMap<K, V> map, int pageSize) {
        super(pageSize);
        this.navigableMap = new TreeMap<>(map);
        this.totalPages = calculateTotalPages();
        this.cursorRef = new AtomicReference<>(initCursor());
    }

    /**
     * 计算总页数（基于初始化时的数据快照）
     */
    private int calculateTotalPages() {
        int size = navigableMap.size();
        return size == 0 ? 0 : (size + pageSize - 1) / pageSize;
    }

    /**
     * 初始化游标（第一页起始键）
     */
    private @NotNull PageCursor<K> initCursor() {
        if (navigableMap.isEmpty()) return PageCursor.empty();
        return new PageCursor<>(navigableMap.firstKey(), 1);
    }

    public boolean nextPage() {
        PageCursor<K> prev;
        PageCursor<K> next;
        do {
            prev = cursorRef.get();
            if (prev.currentPage >= totalPages) return false;

            K nextStart = findNextStartKey(prev.startElement);
            if (nextStart == null) return false;

            int newPage = prev.currentPage + 1;
            next = new PageCursor<>(nextStart, newPage);
        } while (!cursorRef.compareAndSet(prev, next));
        return true;
    }

    public boolean prevPage() {
        PageCursor<K> prev;
        PageCursor<K> next;
        do {
            prev = cursorRef.get();
            if (prev.currentPage <= 1) return false;

            K prevStart = findPrevStartKey(prev.startElement);
            if (prevStart == null) return false;

            int newPage = prev.currentPage - 1;
            next = new PageCursor<>(prevStart, newPage);
        } while (!cursorRef.compareAndSet(prev, next));
        return true;
    }

    public boolean goToPage(int page) {
        if (page < 1 || page > totalPages) return false;

        PageCursor<K> target = calculatePageStart(page);
        if (target == null) return false;

        cursorRef.set(target);
        return true;
    }

    public int getCurrentPage() {
        return cursorRef.get().currentPage;
    }

    public @NotNull List<Entry<K, V>> getCurrentPageElements() {
        PageCursor<K> cursor = cursorRef.get();
        if (cursor.isEmpty()) return Collections.emptyList();

        List<Entry<K, V>> pageEntries = new ArrayList<>(pageSize);
        Iterator<Entry<K, V>> it = navigableMap.tailMap(cursor.startElement, true).entrySet().iterator();

        int count = 0;
        while (it.hasNext() && count < pageSize) {
            pageEntries.add(it.next());
            count++;
        }
        return pageEntries;
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
        return navigableMap.size();
    }

    // ---------------------------- 内部辅助方法 ----------------------------
    private K findNextStartKey(K currentStart) {
        Iterator<K> keyIterator = navigableMap.tailMap(currentStart, false).navigableKeySet().iterator();
        for (int i = 0; i < pageSize - 1 && keyIterator.hasNext(); i++) {
            keyIterator.next();
        }
        return keyIterator.hasNext() ? keyIterator.next() : null;
    }

    private K findPrevStartKey(K currentStart) {
        NavigableMap<K, V> headMap = navigableMap.headMap(currentStart, false);
        if (headMap.isEmpty()) return null;

        Iterator<K> it = headMap.descendingKeySet().iterator();
        int steps = (pageSize - 1) % pageSize; // 计算上一页起始偏移
        for (int i = 0; i < steps && it.hasNext(); i++) {
            it.next();
        }
        return it.hasNext() ? it.next() : navigableMap.firstKey();
    }

    private @Nullable PageCursor<K> calculatePageStart(int targetPage) {
        if (targetPage == 1) return new PageCursor<>(navigableMap.firstKey(), 1);

        Iterator<K> it = navigableMap.navigableKeySet().iterator();
        int skip = (targetPage - 1) * pageSize;

        for (int i = 0; i < skip && it.hasNext(); i++) {
            it.next();
        }
        return it.hasNext() ? new PageCursor<>(it.next(), targetPage) : null;
    }

    public static class Unsafe {
        public static <K, V> @NotNull NavigableMap<K, V> getHandle(NavigableMapPaginationTool<K, V> tool) {
            return tool.navigableMap;
        }
    }
}
