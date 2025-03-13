package pers.neige.neigeitems.utils.pagination;

import org.jetbrains.annotations.Nullable;

/**
 * 游标对象
 */
public class PageCursor<T> {
    private final @Nullable T startElement;
    private final int currentPage;

    public PageCursor(@Nullable T startElement, int currentPage) {
        this.startElement = startElement;
        this.currentPage = currentPage;
    }

    public static <T> PageCursor<T> empty() {
        return new PageCursor<>(null, 0);
    }

    /**
     * 获取当前起始元素
     */
    public @Nullable T getStartElement() {
        return startElement;
    }

    /**
     * 获取当前页码（从1开始）
     */
    public int getCurrentPage() {
        return currentPage;
    }

    public boolean isEmpty() {
        return startElement == null;
    }
}
