package com.sumu.jobscheduler.scheduler.modal.zk;

import java.io.Serializable;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-16 12:44
 */
public class WorkerDataModal implements Serializable {

    private String ip;

    private String app;


    public WorkerDataModal() {

    }

    public WorkerDataModal(String ip, String app) {
        this.ip = ip;
        this.app = app;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }
}
