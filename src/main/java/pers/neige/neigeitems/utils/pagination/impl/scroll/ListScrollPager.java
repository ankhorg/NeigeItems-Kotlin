package pers.neige.neigeitems.utils.pagination.impl.scroll;

import lombok.NonNull;
import lombok.val;
import lombok.var;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.utils.pagination.ScrollPager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class ListScrollPager<T> extends ScrollPager<T> {
    private final @NonNull List<T> handle;
    private final @NonNull AtomicInteger offset = new AtomicInteger(0);

    public ListScrollPager(@NonNull List<T> handle, int pageSize, @Nullable Predicate<T> filter) {
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
            return Math.max(0, Math.min(current + delta, size));
        });
    }

    @Override
    public void moveOffsetByFilter(int delta) {
        if (handle.isEmpty()) return;
        if (filter == null) {
            moveOffset(delta);
            return;
        }
        val size = handle.size();
        val start = Math.min(offset.get(), size - 1);
        val end = size - 1;
        var move = 0;
        var match = 0;
        for (val element : handle.subList(start, end)) {
            if (filter.test(element)) {
                match++;
                if (match >= delta) break;
            }
            move++;
        }
        if (move > 0) moveOffset(move);
    }

    @Override
    public @NonNull List<T> getCurrentPageElements() {
        val size = handle.size();
        if (size == 0) return Collections.emptyList();

        val start = Math.min(offset.get(), size - 1);
        val end = Math.min(start + getPageSize(), size);

        val page = new ArrayList<T>(getPageSize());
        var it = handle.subList(start, end).iterator();

        while (it.hasNext() && page.size() < getPageSize()) {
            val element = it.next();
            if (filter == null || filter.test(element)) page.add(element);
        }
        if (offset.get() == 0) return page;

        if (page.size() < getPageSize()) {
            val delta = getPageSize() - page.size();
            val list = handle.subList(Math.max(0, start - delta), start);

            for (int i = list.size() - 1; i >= 0; i--) {
                if (page.size() >= getPageSize()) break;
                val element = list.get(i);
                if (filter == null || filter.test(element)) page.add(0, element);
            }
        }
        return page;
    }

    @Override
    public int getTotalElements() {
        return handle.size();
    }

    @Override
    public boolean nextPage() {
        if (offset.get() >= (getTotalElements() - 1)) return false;
        moveOffset(getPageSize());
        return true;
    }

    @Override
    public boolean prevPage() {
        if (offset.get() <= 0) return false;
        moveOffset(-getPageSize());
        return true;
    }

    @Override
    public boolean hasNextPage() {
        return offset.get() < (getTotalElements() - 1);
    }

    @Override
    public boolean hasPrevPage() {
        return offset.get() > 0;
    }

    @Override
    public void toFinalOffset() {
        offset.set(getTotalElements() - 1);
    }

    @Override
    protected int getCurrentIndex() {
        return offset.get();
    }
}
