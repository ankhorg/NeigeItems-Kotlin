package pers.neige.neigeitems.utils.pagination.impl.circular;

import lombok.NonNull;
import lombok.val;
import lombok.var;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.utils.pagination.CircularPager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NavigableSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class MutableNavSetCircularPager<T> extends CircularPager<T> {
    private final @NonNull NavigableSet<T> handle;
    private final @NonNull AtomicReference<T> cursor = new AtomicReference<>();

    public MutableNavSetCircularPager(@NonNull NavigableSet<T> handle, int pageSize, @Nullable Predicate<T> filter) {
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

        delta = delta % getTotalElements();

        val reverse = delta < 0;
        val absDelta = Math.abs(delta);

        var it = reverse ?
            handle.headSet(current, false).descendingIterator() :
            handle.tailSet(current, false).iterator();

        // 原子更新游标
        for (int i = 0; i < absDelta; i++) {
            if (!it.hasNext()) it = reverse ?
                handle.descendingIterator() :
                handle.iterator();
            current = it.next();
        }
        cursor.set(current);
    }

    @Override
    public @NonNull List<T> getCurrentPageElements() {
        val current = getCursor();
        if (current == null || handle.isEmpty()) return Collections.emptyList();

        val page = new ArrayList<T>(getPageSize());
        val tail = handle.tailSet(current, true);
        var it = tail.iterator();

        var newRound = false;
        var anyMatch = false;
        List<T> newRoundElements = null;
        while (page.size() < getPageSize()) {
            if (!it.hasNext()) {
                if (newRound) {
                    if (anyMatch) {
                        var roundIterator = newRoundElements.iterator();
                        while (page.size() < getPageSize()) {
                            if (!roundIterator.hasNext()) roundIterator = newRoundElements.iterator();
                            page.add(roundIterator.next());
                        }
                    }
                    return page;
                }
                newRound = true;
                newRoundElements = new ArrayList<>();
                it = handle.iterator();
            }
            val element = it.next();
            if (filter == null || filter.test(element)) {
                if (newRound) {
                    anyMatch = true;
                    newRoundElements.add(element);
                }
                page.add(element);
            }
        }
        return page;
    }

    @Override
    public int getTotalElements() {
        return handle.size();
    }
}
