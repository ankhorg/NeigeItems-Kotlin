package pers.neige.neigeitems.utils.pagination.impl.circular;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.utils.pagination.CircularPager;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class MutableNavSetCircularPager<T> extends CircularPager<T> {
    private final @NotNull NavigableSet<T> handle;
    private final @NotNull AtomicReference<T> cursor = new AtomicReference<>();

    public MutableNavSetCircularPager(@NotNull NavigableSet<T> handle, int pageSize) {
        super(pageSize);
        this.handle = handle;
        resetOffset();
    }

    public @NotNull NavigableSet<T> getHandle() {
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
        T current = getCursor();
        if (current == null) {
            current = handle.first();
        }

        boolean reverse = delta < 0;
        int absDelta = Math.abs(delta);

        Iterator<T> it = reverse ?
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
    public @NotNull List<T> getCurrentPageElements() {
        T current = getCursor();
        if (current == null) return Collections.emptyList();

        List<T> page = new ArrayList<>(pageSize);
        NavigableSet<T> tail = handle.tailSet(current, true);
        Iterator<T> it = tail.iterator();

        for (int i = 0; i < pageSize; i++) {
            if (!it.hasNext()) it = handle.iterator();
            page.add(it.next());
        }
        return page;
    }

    @Override
    public int getTotalElements() {
        return handle.size();
    }
}
