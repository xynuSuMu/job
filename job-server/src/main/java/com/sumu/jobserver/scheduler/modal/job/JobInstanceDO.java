package com.sumu.jobserver.scheduler.modal.job;

import java.util.Date;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-22 14:32
 */
public class JobInstanceDO {
    private int id;
    private int jobDefinitionId;
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

    public int getJobDefinitionId() {
        return jobDefinitionId;
    }

    public void setJobDefinitionId(int jobDefinitionId) {
        this.jobDefinitionId = jobDefinitionId;
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
