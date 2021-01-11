package com.sumu.jobserver.scheduler.interceptor.command.entity.data.worker;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-10 20:39
 */
public interface WorkerEntity extends Worker {

    void setId(int appID);

    void setAppID(int appID);

    void setEnable(Boolean enable);

    void setHostName(String hostName);

    void setIp(String ip);

    void setPort(int port);

    void setZxID(long zxID);
}
