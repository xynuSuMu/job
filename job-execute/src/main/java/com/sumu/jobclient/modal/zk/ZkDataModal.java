package com.sumu.jobclient.modal.zk;

import java.io.Serializable;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 19:29
 */
public class ZkDataModal implements Serializable {

    private String appName;

    private String hostName;

    private String ip;

    private Integer port;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
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

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
