package pers.neige.neigeitems.utils.pagination.impl.circular;

import lombok.NonNull;
import lombok.val;
import lombok.var;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.utils.pagination.CircularPager;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class MutableNavMapCircularPager<K, V> extends CircularPager<Map.Entry<K, V>> {
    private final @NonNull NavigableMap<K, V> handle;
    private final @NonNull AtomicReference<K> cursorKey = new AtomicReference<>();

    public MutableNavMapCircularPager(@NonNull NavigableMap<K, V> handle, int pageSize, @Nullable Predicate<Map.Entry<K, V>> filter) {
        super(pageSize, filter);
        this.handle = handle;
        resetOffset();
    }

    public @NonNull NavigableMap<K, V> getHandle() {
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
        var current = getCursor();
        if (current == null) {
            current = handle.firstKey();
        }

        delta = delta % getTotalElements();

        val reverse = delta < 0;
        val absDelta = Math.abs(delta);

        var it = reverse ?
                handle.headMap(current, false).descendingKeySet().iterator() :
                handle.tailMap(current, false).navigableKeySet().iterator();

        // 原子更新游标
        for (int i = 0; i < absDelta; i++) {
            if (!it.hasNext()) it = reverse ?
                    handle.navigableKeySet().descendingIterator() :
                    handle.navigableKeySet().iterator();
            current = it.next();
        }
        cursorKey.set(current);
    }

    @Override
    public @NonNull List<Map.Entry<K, V>> getCurrentPageElements() {
        val current = getCursor();
        if (current == null || handle.isEmpty()) return Collections.emptyList();

        val page = new ArrayList<Map.Entry<K, V>>(getPageSize());
        val tail = handle.tailMap(current, true);
        var it = tail.entrySet().iterator();

        var newRound = false;
        var anyMatch = false;
        List<Map.Entry<K, V>> newRoundElements = null;
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
                it = handle.entrySet().iterator();
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
