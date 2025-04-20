package pers.neige.neigeitems.utils.pagination.impl.scroll;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.utils.pagination.ScrollPager;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class MutableNavMapScrollPager<K, V> extends ScrollPager<Map.Entry<K, V>> {
    private final @NotNull NavigableMap<K, V> handle;
    private final @NotNull AtomicReference<K> cursorKey = new AtomicReference<>();

    public MutableNavMapScrollPager(@NotNull NavigableMap<K, V> handle, int pageSize, @Nullable Predicate<Map.Entry<K, V>> filter) {
        super(pageSize, filter);
        this.handle = handle;
        resetOffset();
    }

    public @NotNull NavigableMap<K, V> getHandle() {
        return handle;
    }

    /**
     * 获取当前游标
     */
    public K getCursor() {
        K cursor;
        K replacement;
        boolean needReplace;
        do {
            // 当前游标
            cursor = cursorKey.get();
            // 可能的刷新游标
            replacement = null;
            needReplace = false;
            if (cursor == null) {
                // 空游标失效
                if (!handle.isEmpty()) {
                    replacement = handle.firstKey();
                    needReplace = true;
                }
                continue;
            }
            // 清空
            if (handle.isEmpty()) {
                needReplace = true;
                // 元素移除
            } else if (!handle.containsKey(cursor)) {
                replacement = handle.ceilingKey(cursor);
                if (replacement == null) replacement = handle.floorKey(cursor);
                needReplace = true;
            }
        } while (needReplace && !cursorKey.compareAndSet(cursor, replacement));
        return needReplace ? replacement : cursor;
    }

    @Override
    public void resetOffset() {
        cursorKey.set(handle.isEmpty() ? null : handle.firstKey());
    }

    @Override
    public void moveOffset(int delta) {
        if (delta == 0 || handle.isEmpty()) return;
        K current = getCursor();
        if (current == null) {
            current = handle.firstKey();
        }

        boolean reverse = delta < 0;
        int absDelta = Math.abs(delta);

        Iterator<K> it = reverse ?
                handle.headMap(current, false).descendingKeySet().iterator() :
                handle.tailMap(current, false).navigableKeySet().iterator();

        // 原子更新游标
        for (int i = 0; i < absDelta; i++) {
            if (!it.hasNext()) break;
            current = it.next();
        }
        cursorKey.set(current);
    }

    @Override
    public void moveOffsetByFilter(int delta) {
        if (handle.isEmpty()) return;
        if (filter == null) {
            moveOffset(delta);
            return;
        }
        K current = getCursor();
        if (current == null) {
            current = handle.firstKey();
        }
        Iterator<Map.Entry<K, V>> it = handle.tailMap(current, true).entrySet().iterator();
        if (!it.hasNext()) return;
        Map.Entry<K, V> currentEntry = it.next();
        if (filter.test(currentEntry)) return;
        K nextKey = null;
        int match = 0;
        while (it.hasNext()) {
            Map.Entry<K, V> entry = it.next();
            nextKey = entry.getKey();
            if (filter.test(entry)) {
                match++;
                if (match >= delta) break;
            }
        }
        if (nextKey != null) cursorKey.set(nextKey);
    }

    @Override
    public @NotNull List<Map.Entry<K, V>> getCurrentPageElements() {
        K current = getCursor();
        if (current == null || handle.isEmpty()) return Collections.emptyList();

        List<Map.Entry<K, V>> page = new ArrayList<>(getPageSize());
        NavigableMap<K, V> tail = handle.tailMap(current, true);
        Iterator<Map.Entry<K, V>> it = tail.entrySet().iterator();

        while (it.hasNext() && page.size() < getPageSize()) {
            Map.Entry<K, V> element = it.next();
            if (filter == null || filter.test(element)) page.add(element);
        }
        if (current == handle.firstKey()) return page;

        if (page.size() < getPageSize()) {
            NavigableMap<K, V> head = handle.headMap(current, false);
            it = head.descendingMap().entrySet().iterator();

            while (it.hasNext() && page.size() < getPageSize()) {
                Map.Entry<K, V> element = it.next();
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
        C current = (C) getCursor();
        C last = (C) handle.lastKey();
        return current.compareTo(last) >= 0;
    }

    private <C extends Comparable<C>> boolean isFirst() {
        if (handle.isEmpty()) return true;
        C current = (C) getCursor();
        C first = (C) handle.firstKey();
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
        if (handle.isEmpty()) cursorKey.set(null);
        cursorKey.set(handle.firstKey());
    }
}
