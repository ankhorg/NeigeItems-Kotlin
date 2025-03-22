package pers.neige.neigeitems.utils.pagination.impl.circular;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.utils.pagination.CircularPager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class ListCircularPager<T> extends CircularPager<T> {
    private final @NotNull List<T> handle;
    private final @NotNull AtomicInteger offset = new AtomicInteger(0);
    private final @Nullable Predicate<T> filter;

    public ListCircularPager(@NotNull List<T> handle, int pageSize, @Nullable Predicate<T> filter) {
        super(pageSize);
        this.handle = handle;
        this.filter = filter;
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
            return (current + delta) % size;
        });
    }

    @Override
    public @NotNull List<T> getCurrentPageElements() {
        final int size = handle.size();
        if (size == 0) return Collections.emptyList();

        final int start = Math.min(offset.get(), size - 1);
        final int end = Math.min(start + pageSize, size);

        List<T> page = new ArrayList<>(pageSize);
        Iterator<T> it = handle.subList(start, end).iterator();

        boolean newRound = false;
        boolean anyMatch = false;
        List<T> newRoundElements = null;
        while (page.size() < pageSize) {
            if (!it.hasNext()) {
                if (newRound) {
                    if (anyMatch) {
                        Iterator<T> roundIterator = newRoundElements.iterator();
                        while (page.size() < pageSize) {
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
            T element = it.next();
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
