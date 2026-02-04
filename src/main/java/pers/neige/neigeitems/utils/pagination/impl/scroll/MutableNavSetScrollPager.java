package pers.neige.neigeitems.utils.pagination.impl.scroll;

import lombok.NonNull;
import lombok.val;
import lombok.var;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.utils.pagination.ScrollPager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NavigableSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class MutableNavSetScrollPager<T> extends ScrollPager<T> {
    private final @NonNull NavigableSet<T> handle;
    private final @NonNull AtomicReference<T> cursor = new AtomicReference<>();

    public MutableNavSetScrollPager(@NonNull NavigableSet<T> handle, int pageSize, @Nullable Predicate<T> filter) {
        super(pageSize, filter);
        this.handle = handle;
        resetOffset();
    }

    public @NonNull NavigableSet<T> getHandle() {
        return handle;
    }

    /**
     * 获取当前游标
     */
    public T getCursor() {
        T cursor;
        T replacement;
        boolean needReplace;
        do {
            // 当前游标
            cursor = this.cursor.get();
            // 可能的刷新游标
            replacement = null;
            needReplace = false;
            if (cursor == null) {
                // 空游标失效
                if (!handle.isEmpty()) {
                    replacement = handle.first();
                    needReplace = true;
                }
                continue;
            }
            // 清空
            if (handle.isEmpty()) {
                needReplace = true;
                // 元素移除
            } else if (!handle.contains(cursor)) {
                replacement = handle.ceiling(cursor);
                if (replacement == null) replacement = handle.floor(cursor);
                needReplace = true;
            }
        } while (needReplace && !this.cursor.compareAndSet(cursor, replacement));
        return needReplace ? replacement : cursor;
    }

    @Override
    public void resetOffset() {
        cursor.set(handle.isEmpty() ? null : handle.first());
    }

    @Override
    public void moveOffset(int delta) {
        if (delta == 0 || handle.isEmpty()) return;
        var current = getCursor();
        if (current == null) {
            current = handle.first();
        }

        val reverse = delta < 0;
        val absDelta = Math.abs(delta);

        var it = reverse ?
                handle.headSet(current, false).descendingIterator() :
                handle.tailSet(current, false).iterator();

        // 原子更新游标
        for (int i = 0; i < absDelta; i++) {
            if (!it.hasNext()) break;
            current = it.next();
        }
        cursor.set(current);
    }

    @Override
    public void moveOffsetByFilter(int delta) {
        if (handle.isEmpty()) return;
        if (filter == null) {
            moveOffset(delta);
            return;
        }
        var current = getCursor();
        if (current == null) {
            current = handle.first();
        }
        if (filter.test(current)) return;
        T next = null;
        var match = 0;
        for (val element : handle.tailSet(current, false)) {
            next = element;
            if (filter.test(element)) {
                match++;
                if (match >= delta) break;
            }
        }
        if (next != null) cursor.set(next);
    }

    @Override
    public @NonNull List<T> getCurrentPageElements() {
        val current = getCursor();
        if (current == null || handle.isEmpty()) return Collections.emptyList();

        val page = new ArrayList<T>(getPageSize());
        val tail = handle.tailSet(current, true);
        var it = tail.iterator();

        while (it.hasNext() && page.size() < getPageSize()) {
            val element = it.next();
            if (filter == null || filter.test(element)) page.add(element);
        }
        if (current == handle.first()) return page;

        if (page.size() < getPageSize()) {
            val head = handle.headSet(current, false);
            it = head.descendingIterator();

            while (it.hasNext() && page.size() < getPageSize()) {
                val element = it.next();
                if (filter == null || filter.test(element)) page.add(0, element);
            }
        }
        return page;
    }

    @Override
    public int getTotalElements() {
        return handle.size();
    }

    private <C extends Comparable<C>> boolean isLast() {
        if (handle.isEmpty()) return true;
        val current = (C) getCursor();
        val last = (C) handle.last();
        return current.compareTo(last) >= 0;
    }

    private <C extends Comparable<C>> boolean isFirst() {
        if (handle.isEmpty()) return true;
        val current = (C) getCursor();
        val first = (C) handle.first();
        return current.compareTo(first) <= 0;
    }

    @Override
    public boolean nextPage() {
        if (isLast()) return false;
        moveOffset(getPageSize());
        return true;
    }

    @Override
    public boolean prevPage() {
        if (isFirst()) return false;
        moveOffset(-getPageSize());
        return true;
    }

    @Override
    public boolean hasNextPage() {
        return !isLast();
    }

    @Override
    public boolean hasPrevPage() {
        return !isFirst();
    }

    @Override
    public void toFinalOffset() {
        if (handle.isEmpty()) cursor.set(null);
        cursor.set(handle.last());
    }

    @Override
    protected int getCurrentIndex() {
        val cur = getCursor();
        if (cur == null || handle.isEmpty()) return 0;

        return handle.headSet(cur, false).size();
    }
}
