package pers.neige.neigeitems.utils.pagination;

/**
 * 分页工具信息
 */
public class PagerInfo {
    private final int pageSize;
    private volatile int cachedTotalElements = 0;
    private volatile int cachedTotalPages = 0;

    public PagerInfo(int pageSize) {
        this.pageSize = pageSize;
    }

    private static int calculateTotalPages(int totalElements, int pageSize) {
        if (totalElements == 0) return 0;
        return (totalElements + pageSize - 1) / pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPages(int totalElements) {
        if (cachedTotalElements != totalElements) {
            synchronized (this) {
                if (cachedTotalElements != totalElements) {
                    cachedTotalElements = totalElements;
                    cachedTotalPages = calculateTotalPages(totalElements, pageSize);
                }
            }
        }
        return cachedTotalPages;
    }
}
