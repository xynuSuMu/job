package com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.instance;

import java.util.Date;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-12 16:10
 */
public class JobInstanceEntityImpl implements JobInstanceEntity {

    private int id;
    private int jobDefinitionId;
    private Date startTime;
    private Date endTime;
    private int triggerType;
    private String triggerWorker;
    private String triggerResult;

    public int getId() {
        return id;
    }

    public int getJobDefinitionId() {
        return jobDefinitionId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public int getTriggerType() {
        return triggerType;
    }

    public String getTriggerWorker() {
        return triggerWorker;
    }

    public String getTriggerResult() {
        return triggerResult;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setJobDefinitionId(int jobDefinitionId) {
        this.jobDefinitionId = jobDefinitionId;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setTriggerType(int triggerType) {
        this.triggerType = triggerType;
    }

    public void setTriggerWorker(String triggerWorker) {
        this.triggerWorker = triggerWorker;
    }

    public void setTriggerResult(String triggerResult) {
        this.triggerResult = triggerResult;
    }
}
