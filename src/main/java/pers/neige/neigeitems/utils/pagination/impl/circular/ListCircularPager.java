package pers.neige.neigeitems.utils.pagination.impl.circular;

import lombok.NonNull;
import lombok.val;
import lombok.var;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.utils.pagination.CircularPager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class ListCircularPager<T> extends CircularPager<T> {
    private final @NonNull List<T> handle;
    private final @NonNull AtomicInteger offset = new AtomicInteger(0);

    public ListCircularPager(@NonNull List<T> handle, int pageSize, @Nullable Predicate<T> filter) {
        super(pageSize, filter);
        this.handle = handle;
    }

    public @NonNull List<T> getHandle() {
        return handle;
    }

    @Override
    public void resetOffset() {
        offset.set(0);
    }

    @Override
    public void moveOffset(int delta) {
        offset.updateAndGet(current -> {
            val size = handle.size();
            if (size == 0) return 0;
            return (current + delta) % size;
        });
    }

    @Override
    public @NonNull List<T> getCurrentPageElements() {
        val size = handle.size();
        if (size == 0) return Collections.emptyList();

        val start = Math.min(offset.get(), size - 1);
        val end = Math.min(start + getPageSize(), size);

        val page = new ArrayList<T>(getPageSize());
        var it = handle.subList(start, end).iterator();

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
