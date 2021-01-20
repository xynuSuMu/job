package com.sumu.jobserver.api.vo;

import java.util.Date;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-22 15:21
 */
public class JobInstanceVO {

    private int id;
    private String startTime;
    private String endTime;
    private int triggerType;
    private String triggerWorker;
    private String triggerResult;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
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

    public String getTriggerResult() {
        return triggerResult;
    }

    public void setTriggerResult(String triggerResult) {
        this.triggerResult = triggerResult;
    }
}
