package com.sumu.jobserver.scheduler.interceptor.command.entity.data.app;

import java.util.Date;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-11 11:01
 */
public interface App {

    int getID();

    String getAppCode();

    long getZxID();

    Date getUpdateTime();
}
