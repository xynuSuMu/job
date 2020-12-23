package com.sumu.jobserver.modal.worker;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-22 20:09
 */
public class WorkerDO {

    private int id;
    private int appID;
    private String ip;
    private String port;
    private String hostName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppID() {
        return appID;
    }

    public void setAppID(int appID) {
        this.appID = appID;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
