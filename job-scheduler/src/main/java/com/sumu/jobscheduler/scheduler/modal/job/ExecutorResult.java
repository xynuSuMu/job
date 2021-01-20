package com.sumu.jobscheduler.scheduler.modal.job;

import java.util.Date;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-20 10:39
 */
public class ExecutorResult {

    private Date endTime;

    private String worker;

    private String result;

    public ExecutorResult(Date endTime, String worker, String result) {
        this.endTime = endTime;
        this.worker = worker;
        this.result = result;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
