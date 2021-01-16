package com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.app;

import java.util.Date;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-11 11:03
 */
public interface AppEntity extends App {

    void setID(int id);

    void setAppCode(String appCode);

    void setZxID(long zxID);

    void setUpdateTime(Date updateTime);

}
