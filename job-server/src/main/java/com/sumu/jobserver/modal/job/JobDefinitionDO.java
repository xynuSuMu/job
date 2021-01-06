package com.sumu.jobserver.modal.job;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-22 19:52
 */
public class JobDefinitionDO {

    private int id;
    private int appId;
    private String jobName;
    private String jobDesc;
    private String cron;
    private int taskType;
    private Boolean enable;
    private String postDefinitionID;

    public String getPostDefinitionID() {
        return postDefinitionID;
    }

    public void setPostDefinitionID(String postDefinitionID) {
        this.postDefinitionID = postDefinitionID;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }
}
