package pers.neige.neigeitems.utils.pagination;

/**
 * 分页工具信息
 */
public class PageToolInfo {
    private final int pageSize;
    private volatile int preTotalElements = 0;
    private volatile int preTotalPages = 0;

    public PageToolInfo(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPages(int totalElements) {
        if (preTotalElements != totalElements) {
            synchronized (this) {
                if (preTotalElements != totalElements) {
                    this.preTotalElements = totalElements;
                    int size = this.preTotalElements;
                    // 更新当前页数（总页数为0时设为0，否则从1开始）
                    this.preTotalPages = size == 0 ? 0 : (size + this.pageSize - 1) / this.pageSize;
                }
            }
        }
        return this.preTotalPages;
    }
}
