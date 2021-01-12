package com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.definition;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-12 10:44
 */
public class JobDefinitionEntityImpl implements JobDefinitionEntity {
    private int id;
    private int appId;
    private String jobName;
    private String jobDesc;
    private String cron;
    private int taskType;
    private Boolean enable;
    private String postDefinitionID;

    public void setId(int id) {
        this.id = id;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public void setPostDefinitionID(String postDefinitionID) {
        this.postDefinitionID = postDefinitionID;
    }

    public int getId() {
        return id;
    }

    public int getAppId() {
        return appId;
    }

    public String getJobName() {
        return jobName;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public String getCron() {
        return cron;
    }

    public int getTaskType() {
        return taskType;
    }

    public Boolean getEnable() {
        return enable;
    }

    public String getPostDefinitionID() {
        return postDefinitionID;
    }
}
