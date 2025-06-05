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
import java.util.NavigableSet;
import java.util.concurrent.atomic.AtomicReference;

public class MutableNavSetPager<T> extends Pager<T> {
    /**
     * 可变数据
     */
    private final @NonNull NavigableSet<T> handle;
    /**
     * 当前页游标
     */
    private final @NonNull AtomicReference<PageCursor<T>> cursorRef;

    public MutableNavSetPager(@NonNull NavigableSet<T> set, int pageSize) {
        super(pageSize);
        this.handle = set;
        this.cursorRef = new AtomicReference<>(initCursor());
    }

    public @NonNull NavigableSet<T> getHandle() {
        return handle;
    }

    /**
     * 初始化游标位置（第一页起始键）
     */
    private @NonNull PageCursor<T> initCursor() {
        if (handle.isEmpty()) return PageCursor.empty();
        return new PageCursor<>(handle.first(), 1);
    }

    public boolean nextPage() {
        PageCursor<T> prev;
        PageCursor<T> next;
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
        PageCursor<T> prev;
        PageCursor<T> next;
        do {
            prev = getCursor();
            if (prev.getCurrentPage() <= 1) {
                if (!handle.isEmpty() && prev.getCurrentPage() == 1 && prev.getStartElement() != handle.first()) {
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
    public PageCursor<T> getCursor() {
        PageCursor<T> cursor;
        PageCursor<T> maybeReplacement;
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
            } else if (!handle.contains(cursor.getStartElement())) {
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

    public @NonNull List<T> getCurrentPageElements() {
        if (handle.isEmpty()) return Collections.emptyList();
        val cursor = getCursor();
        if (cursor.isEmpty()) return Collections.emptyList();

        val pageElements = new ArrayList<T>(getPageSize());
        val it = handle.tailSet(cursor.getStartElement(), true).iterator();

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

    private T findNextStart(T currentStart) {
        val it = handle.tailSet(currentStart, false).iterator();
        for (int i = 0; i < getPageSize() - 1 && it.hasNext(); i++) {
            it.next();
        }
        return it.hasNext() ? it.next() : null;
    }

    private T findPrevStart(T currentStart) {
        if (handle.isEmpty()) return null;
        val headSet = handle.headSet(currentStart, false);
        if (headSet.isEmpty()) return null;

        val it = headSet.descendingIterator();
        val steps = (getPageSize() - 1) % getPageSize(); // 计算上一页起始偏移
        for (int i = 0; i < steps && it.hasNext(); i++) {
            it.next();
        }
        return it.hasNext() ? it.next() : handle.first();
    }

    private @Nullable PageCursor<T> calculatePageStart(int targetPage) {
        // 范围检测
        if (targetPage < 1 || targetPage > getTotalPages()) return null;

        val totalSize = handle.size();
        if (totalSize == 0) return null;

        val skip = (targetPage - 1) * getPageSize();
        // 反向遍历逻辑
        if (targetPage > (getTotalPages() / 2)) {
            val reverseSkip = totalSize - skip - 1; // 计算反向跳过的位置
            if (reverseSkip < 0) return null;

            val it = handle.descendingIterator();
            for (int i = 0; i < reverseSkip && it.hasNext(); i++) {
                it.next();
            }

            return it.hasNext() ? new PageCursor<>(it.next(), targetPage) : null;
        } else {
            // 正向遍历逻辑
            val it = handle.iterator();
            for (int i = 0; i < skip && it.hasNext(); i++) {
                it.next();
            }
            return it.hasNext() ? new PageCursor<>(it.next(), targetPage) : null;
        }
    }
}
