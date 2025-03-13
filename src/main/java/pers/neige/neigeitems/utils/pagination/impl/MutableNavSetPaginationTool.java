package pers.neige.neigeitems.utils.pagination.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.utils.pagination.PageCursor;
import pers.neige.neigeitems.utils.pagination.PaginationTool;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class MutableNavSetPaginationTool<T> extends PaginationTool<T> {
    /**
     * 可变数据
     */
    private final @NotNull NavigableSet<T> handle;
    /**
     * 当前页游标
     */
    private final @NotNull AtomicReference<PageCursor<T>> cursorRef;

    public MutableNavSetPaginationTool(@NotNull NavigableSet<T> set, int pageSize) {
        super(pageSize);
        this.handle = set;
        this.cursorRef = new AtomicReference<>(initCursor());
    }

    public @NotNull NavigableSet<T> getHandle() {
        return handle;
    }

    /**
     * 初始化游标位置（第一页起始键）
     */
    private @NotNull PageCursor<T> initCursor() {
        if (handle.isEmpty()) return PageCursor.empty();
        return new PageCursor<>(handle.first(), 1);
    }

    public boolean nextPage() {
        PageCursor<T> prev;
        PageCursor<T> next;
        do {
            prev = getCursor();
            if (prev.getCurrentPage() >= getTotalPages()) return false;

            T nextStart = findNextStart(prev.getStartElement());
            if (nextStart == null) return false;

            int newPage = prev.getCurrentPage() + 1;
            next = new PageCursor<>(nextStart, newPage);
        } while (!cursorRef.compareAndSet(prev, next));
        return true;
    }

    public boolean prevPage() {
        PageCursor<T> prev;
        PageCursor<T> next;
        do {
            prev = getCursor();
            if (prev.getCurrentPage() <= 1) return false;

            T prevStart = findPrevStart(prev.getStartElement());
            if (prevStart == null) return false;

            int newPage = prev.getCurrentPage() - 1;
            next = new PageCursor<>(prevStart, newPage);
        } while (!cursorRef.compareAndSet(prev, next));
        return true;
    }

    public boolean goToPage(int page) {
        if (page < 1 || page > getTotalPages()) return false;

        PageCursor<T> target = calculatePageStart(page);
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

    public @NotNull List<T> getCurrentPageElements() {
        if (handle.isEmpty()) return Collections.emptyList();
        PageCursor<T> cursor = getCursor();
        if (cursor.isEmpty()) return Collections.emptyList();

        List<T> pageElements = new ArrayList<>(getPageSize());
        Iterator<T> it = handle.tailSet(cursor.getStartElement(), true).iterator();

        int count = 0;
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
        Iterator<T> it = handle.tailSet(currentStart, false).iterator();
        for (int i = 0; i < getPageSize() - 1 && it.hasNext(); i++) {
            it.next();
        }
        return it.hasNext() ? it.next() : null;
    }

    private T findPrevStart(T currentStart) {
        if (handle.isEmpty()) return null;
        NavigableSet<T> headSet = handle.headSet(currentStart, false);
        if (headSet.isEmpty()) return null;

        Iterator<T> it = headSet.descendingIterator();
        int steps = (getPageSize() - 1) % getPageSize(); // 计算上一页起始偏移
        for (int i = 0; i < steps && it.hasNext(); i++) {
            it.next();
        }
        return it.hasNext() ? it.next() : handle.first();
    }

    private @Nullable PageCursor<T> calculatePageStart(int targetPage) {
        // 范围检测
        if (targetPage < 1 || targetPage > getTotalPages()) return null;

        final int totalSize = handle.size();
        if (totalSize == 0) return null;

        final int skip = (targetPage - 1) * getPageSize();
        // 反向遍历逻辑
        if (targetPage > (getTotalPages() / 2)) {
            final int reverseSkip = totalSize - skip - 1; // 计算反向跳过的位置
            if (reverseSkip < 0) return null;

            Iterator<T> it = handle.descendingIterator();
            for (int i = 0; i < reverseSkip && it.hasNext(); i++) {
                it.next();
            }

            return it.hasNext() ? new PageCursor<>(it.next(), targetPage) : null;
        } else {
            // 正向遍历逻辑
            Iterator<T> it = handle.iterator();
            for (int i = 0; i < skip && it.hasNext(); i++) {
                it.next();
            }
            return it.hasNext() ? new PageCursor<>(it.next(), targetPage) : null;
        }
    }
}
