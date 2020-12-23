package com.sumu.jobserver.api.vo;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-22 13:33
 */
public class JobDefinitionVO {

    private String appName;
    private String jobName;
    private String handlerName;
    private String cron;
    private String lastExecuteTime;
    private Boolean enable;


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

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
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
}
