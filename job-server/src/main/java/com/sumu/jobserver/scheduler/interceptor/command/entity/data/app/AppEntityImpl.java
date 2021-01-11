package com.sumu.jobserver.scheduler.interceptor.command.entity.data.app;

import java.util.Date;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-11 11:03
 */
public class AppEntityImpl implements AppEntity {

    private int id;
    private String appCode;
    private long zxID;
    private Date updateTime;

    @Override
    public void setID(int id) {
        this.id = id;
    }

    @Override
    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    @Override
    public void setZxID(long zxID) {
        this.zxID = zxID;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public String getAppCode() {
        return appCode;
    }

    @Override
    public long getZxID() {
        return zxID;
    }

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }
}
