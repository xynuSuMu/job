package com.sumu.jobserver.api.vo.query;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-22 20:36
 */
public class JobDefinitionQuery {

    private int appID;
    private int pageIndex;
    private int pageSize;


    public int getAppID() {
        return appID;
    }

    public void setAppID(int appID) {
        this.appID = appID;
    }


    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
