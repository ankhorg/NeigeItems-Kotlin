package pers.neige.neigeitems.utils.pagination.impl.circular;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.utils.pagination.CircularPager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MutableListCircularPager<T> extends CircularPager<T> {
    private final @NotNull List<T> handle;
    private final @NotNull AtomicInteger offset = new AtomicInteger(0);

    public MutableListCircularPager(@NotNull List<T> handle, int pageSize) {
        super(pageSize);
        this.handle = handle;
    }

    public @NotNull List<T> getHandle() {
        return handle;
    }

    @Override
    public void resetOffset() {
        offset.set(0);
    }

    @Override
    public void moveOffset(int delta) {
        offset.updateAndGet(current -> {
            int size = handle.size();
            if (size == 0) return 0;
            return Math.floorMod(current + delta, size);
        });
    }

    @Override
    public @NotNull List<T> getCurrentPageElements() {
        final int currentOffset = offset.get();
        final int size = handle.size();
        if (size == 0) return Collections.emptyList();

        List<T> page = new ArrayList<>(pageSize);
        for (int i = 0; i < pageSize; i++) {
            int index = Math.floorMod(currentOffset + i, size);
            if (index < handle.size()) { // 检查动态变化后的索引有效性
                page.add(handle.get(index));
            } else {
                // 处理列表缩短的情况
                index = Math.floorMod(index, handle.size());
                page.add(handle.get(index));
            }
        }
        return page;
    }

    @Override
    public int getTotalElements() {
        return handle.size();
    }
}
