package pers.neige.neigeitems.utils.pagination.impl;

import lombok.NonNull;
import lombok.val;
import lombok.var;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.utils.pagination.PageCursor;
import pers.neige.neigeitems.utils.pagination.Pager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.concurrent.atomic.AtomicReference;

public class MutableNavMapPager<K, V> extends Pager<Entry<K, V>> {
    /**
     * 可变数据
     */
    private final @NonNull NavigableMap<K, V> handle;
    /**
     * 当前页游标
     */
    private final @NonNull AtomicReference<PageCursor<K>> cursorRef;

    public MutableNavMapPager(@NonNull NavigableMap<K, V> set, int pageSize) {
        super(pageSize);
        this.handle = set;
        this.cursorRef = new AtomicReference<>(initCursor());
    }

    public @NonNull NavigableMap<K, V> getHandle() {
        return handle;
    }

    /**
     * 初始化游标位置（第一页起始键）
     */
    private @NonNull PageCursor<K> initCursor() {
        if (handle.isEmpty()) return PageCursor.empty();
        return new PageCursor<>(handle.firstKey(), 1);
    }

    public boolean nextPage() {
        PageCursor<K> prev;
        PageCursor<K> next;
        do {
            prev = getCursor();
            if (prev.getCurrentPage() >= getTotalPages()) return false;

            val nextStart = findNextStart(prev.getStartElement());
            if (nextStart == null) return false;

            val newPage = prev.getCurrentPage() + 1;
            next = new PageCursor<>(nextStart, newPage);
        } while (!cursorRef.compareAndSet(prev, next));
        return true;
    }

    public boolean prevPage() {
        PageCursor<K> prev;
        PageCursor<K> next;
        do {
            prev = getCursor();
            if (prev.getCurrentPage() <= 1) {
                if (!handle.isEmpty() && prev.getCurrentPage() == 1 && prev.getStartElement() != handle.firstKey()) {
                    goToPage(1);
                    return true;
                } else {
                    return false;
                }
            }

            val prevStart = findPrevStart(prev.getStartElement());
            if (prevStart == null) return false;

            val newPage = prev.getCurrentPage() - 1;
            next = new PageCursor<>(prevStart, newPage);
        } while (!cursorRef.compareAndSet(prev, next));
        return true;
    }

    public boolean goToPage(int page) {
        if (page < 1 || page > getTotalPages()) return false;

        val target = calculatePageStart(page);
        if (target == null) return false;

        cursorRef.set(target);
        return true;
    }

    /**
     * 获取当前游标
     */
    public PageCursor<K> getCursor() {
        PageCursor<K> cursor;
        PageCursor<K> maybeReplacement;
        do {
            // 当前游标
            cursor = cursorRef.get();
            // 可能的刷新游标
            maybeReplacement = null;
            if (cursor.getStartElement() == null) {
                // 空游标失效
                if (!handle.isEmpty()) {
                    maybeReplacement = initCursor();
                }
                continue;
            }
            // 清空
            if (handle.isEmpty()) {
                maybeReplacement = PageCursor.empty();
                // 元素移除
            } else if (!handle.containsKey(cursor.getStartElement())) {
                // 刷新页
                goToPage(Math.min(getTotalPages(), cursor.getCurrentPage()));
                cursor = cursorRef.get();
            }
        } while (maybeReplacement != null && !cursorRef.compareAndSet(cursor, maybeReplacement));
        return maybeReplacement != null ? maybeReplacement : cursor;
    }

    public int getCurrentPage() {
        return getCursor().getCurrentPage();
    }

    public @NonNull List<Entry<K, V>> getCurrentPageElements() {
        if (handle.isEmpty()) return Collections.emptyList();
        val cursor = getCursor();
        if (cursor.isEmpty()) return Collections.emptyList();

        val pageElements = new ArrayList<Entry<K, V>>(getPageSize());
        val it = handle.tailMap(cursor.getStartElement(), true).entrySet().iterator();

        var count = 0;
        while (it.hasNext() && count < getPageSize()) {
            pageElements.add(it.next());
            count++;
        }
        return pageElements;
    }

    public boolean hasNextPage() {
        return cursorRef.get().getCurrentPage() < getTotalPages();
    }

    public boolean hasPrevPage() {
        return cursorRef.get().getCurrentPage() > 1;
    }

    public int getTotalElements() {
        return handle.size();
    }

    private K findNextStart(K currentStart) {
        val it = handle.tailMap(currentStart, false).navigableKeySet().iterator();
        for (int i = 0; i < getPageSize() - 1 && it.hasNext(); i++) {
            it.next();
        }
        return it.hasNext() ? it.next() : null;
    }

    private K findPrevStart(K currentStart) {
        if (handle.isEmpty()) return null;
        val headSet = handle.headMap(currentStart, false).navigableKeySet();
        if (headSet.isEmpty()) return null;

        val it = headSet.descendingIterator();
        val steps = (getPageSize() - 1) % getPageSize(); // 计算上一页起始偏移
        for (int i = 0; i < steps && it.hasNext(); i++) {
            it.next();
        }
        return it.hasNext() ? it.next() : handle.firstKey();
    }

    private @Nullable PageCursor<K> calculatePageStart(int targetPage) {
        // 范围检测
        if (targetPage < 1 || targetPage > getTotalPages()) return null;

        val totalSize = handle.size();
        if (totalSize == 0) return null;

        val skip = (targetPage - 1) * getPageSize();
        // 反向遍历逻辑
        if (targetPage > (getTotalPages() / 2)) {
            val reverseSkip = totalSize - skip - 1; // 计算反向跳过的位置
            if (reverseSkip < 0) return null;

            val it = handle.navigableKeySet().descendingIterator();
            for (int i = 0; i < reverseSkip && it.hasNext(); i++) {
                it.next();
            }

            return it.hasNext() ? new PageCursor<>(it.next(), targetPage) : null;
        } else {
            // 正向遍历逻辑
            val it = handle.navigableKeySet().iterator();
            for (int i = 0; i < skip && it.hasNext(); i++) {
                it.next();
            }
            return it.hasNext() ? new PageCursor<>(it.next(), targetPage) : null;
        }
    }
}
