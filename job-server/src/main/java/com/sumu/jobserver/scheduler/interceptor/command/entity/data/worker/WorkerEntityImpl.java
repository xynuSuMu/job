package com.sumu.jobserver.scheduler.interceptor.command.entity.data.worker;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-10 20:42
 */
public class WorkerEntityImpl implements WorkerEntity {
    private int id;
    private int appId;
    private String hostName;
    private String ip;
    private int port;
    private long zxID;

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setAppID(int appID) {
        this.appId = appID;
    }

    @Override
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    @Override
    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void setZxID(long zxID) {
        this.zxID = zxID;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public int getAppId() {
        return appId;
    }

    @Override
    public String getHostName() {
        return hostName;
    }

    @Override
    public String getIp() {
        return ip;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public long getZxID() {
        return zxID;
    }
}
