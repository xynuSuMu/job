package com.sumu.jobserver.api.vo;

import java.util.Date;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-22 15:21
 */
public class JobInstanceVO {

    private int id;
    private Date startTime;
    private Date endTime;
    private int triggerType;
    private String triggerWorker;
    private int triggerResult;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(int triggerType) {
        this.triggerType = triggerType;
    }

    public String getTriggerWorker() {
        return triggerWorker;
    }

    public void setTriggerWorker(String triggerWorker) {
        this.triggerWorker = triggerWorker;
    }

    public int getTriggerResult() {
        return triggerResult;
    }

    public void setTriggerResult(int triggerResult) {
        this.triggerResult = triggerResult;
    }
}
