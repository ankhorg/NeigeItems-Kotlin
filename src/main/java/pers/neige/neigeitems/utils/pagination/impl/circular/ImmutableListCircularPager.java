package pers.neige.neigeitems.utils.pagination.impl.circular;

import org.jetbrains.annotations.NotNull;
import pers.neige.neigeitems.utils.pagination.CircularPager;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ImmutableListCircularPager<T> extends CircularPager<T> {
    private final @NotNull List<T> handle;
    private final @NotNull AtomicInteger cursor = new AtomicInteger(0);

    public ImmutableListCircularPager(@NotNull List<T> handle, int pageSize) {
        super(pageSize);
        this.handle = handle;
    }

    public @NotNull List<T> getHandle() {
        return handle;
    }

    @Override
    public void resetOffset() {
        cursor.set(0);
    }

    @Override
    public void moveOffset(int delta) {
        cursor.updateAndGet(current -> {
            int size = handle.size();
            return size == 0 ? 0 : (current + delta % size + size) % size;
        });
    }

    @Override
    public @NotNull List<T> getCurrentPageElements() {
        final int size = handle.size();
        if (size == 0) return Collections.emptyList();

        final int start = cursor.get() % size;
        final int endIdx = Math.min(start + pageSize, size);

        List<T> page = handle.subList(start, endIdx);
        if (endIdx < start + pageSize) {
            page.addAll(handle.subList(0, (start + pageSize) % size));
        }
        return page;
    }

    @Override
    public int getTotalElements() {
        return handle.size();
    }
}
