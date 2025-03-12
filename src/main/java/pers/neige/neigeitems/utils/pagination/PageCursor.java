package pers.neige.neigeitems.utils.pagination;

/**
 * 游标对象
 */
public class PageCursor<T> {
    public final T startElement;
    public final int currentPage;

    public PageCursor(T startElement, int currentPage) {
        this.startElement = startElement;
        this.currentPage = currentPage;
    }

    public static <T> PageCursor<T> empty() {
        return new PageCursor<>(null, 0);
    }

    public boolean isEmpty() {
        return startElement == null;
    }
}
