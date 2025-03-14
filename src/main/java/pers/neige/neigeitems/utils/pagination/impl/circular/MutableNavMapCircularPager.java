package pers.neige.neigeitems.utils.pagination.impl.circular;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.utils.pagination.CircularPager;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class MutableNavMapCircularPager<K, V> extends CircularPager<Map.Entry<K, V>> {
    private final @NotNull NavigableMap<K, V> handle;
    private final @NotNull AtomicReference<K> cursorKey = new AtomicReference<>();

    public MutableNavMapCircularPager(@NotNull NavigableMap<K, V> handle, int pageSize) {
        super(pageSize);
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
            if (!it.hasNext()) it = reverse ?
                    handle.navigableKeySet().descendingIterator() :
                    handle.navigableKeySet().iterator();
            current = it.next();
        }
        cursorKey.set(current);
    }

    @Override
    public @NotNull List<Map.Entry<K, V>> getCurrentPageElements() {
        K current = getCursor();
        if (current == null) return Collections.emptyList();

        List<Map.Entry<K, V>> page = new ArrayList<>(pageSize);
        NavigableMap<K, V> tail = handle.tailMap(current, true);
        Iterator<Map.Entry<K, V>> it = tail.entrySet().iterator();

        for (int i = 0; i < pageSize; i++) {
            if (!it.hasNext()) it = handle.entrySet().iterator();
            page.add(it.next());
        }
        return page;
    }

    @Override
    public int getTotalElements() {
        return handle.size();
    }
}
