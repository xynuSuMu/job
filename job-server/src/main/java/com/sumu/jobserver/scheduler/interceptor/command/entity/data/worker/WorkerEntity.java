package com.sumu.jobserver.scheduler.interceptor.command.entity.data.worker;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-10 20:39
 */
public interface WorkerEntity extends Worker {

    void setAppID(int appID);

    void setHostName(String hostName);

    void setIp(String ip);

    void setPort(int port);

    void setZxID(long zxID);
}
