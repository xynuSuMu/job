package com.sumu.jobserver.scheduler.interceptor.command.entity.data.worker;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-10 20:49
 */
public interface Worker {

    int getID();

    int getAppId();

    String getHostName();

    String getIp();

    int getPort();

    long getZxID();
}
