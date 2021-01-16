package com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.common;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-12 11:32
 */
public class Page {

    private int pageIndex;
    private int pageSize;

    public Page(int pageIndex, int pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }
}
