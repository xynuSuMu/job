package com.sumu.jobserver.api.vo;

import com.sumu.jobserver.api.vo.param.JavaJobVO;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-22 13:33
 */
public class JobDefinitionVO {

    private int id;
    private String appName;
    private int appId;
    private String jobName;
    private String jobDesc;
    private String cron;
    private String lastExecuteTime;
    private int taskType;
    private Boolean enable;

    private JavaJobVO javaJobVO;

    //后置任务
    private String postDefinitionID;

    public String getPostDefinitionID() {
        return postDefinitionID;
    }

    public void setPostDefinitionID(String postDefinitionID) {
        this.postDefinitionID = postDefinitionID;
    }


    public JavaJobVO getJavaJobVO() {
        return javaJobVO;
    }

    public void setJavaJobVO(JavaJobVO javaJobVO) {
        this.javaJobVO = javaJobVO;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }


    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getLastExecuteTime() {
        return lastExecuteTime;
    }

    public void setLastExecuteTime(String lastExecuteTime) {
        this.lastExecuteTime = lastExecuteTime;
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
