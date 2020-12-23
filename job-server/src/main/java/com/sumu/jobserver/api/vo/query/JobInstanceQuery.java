package com.sumu.jobserver.api.vo.query;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-22 15:11
 */
public class JobInstanceQuery {

    private int jonDefinitionID;

    private int pageIndex;

    private int pageSize;

    public int getJonDefinitionID() {
        return jonDefinitionID;
    }

    public void setJonDefinitionID(int jonDefinitionID) {
        this.jonDefinitionID = jonDefinitionID;
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
