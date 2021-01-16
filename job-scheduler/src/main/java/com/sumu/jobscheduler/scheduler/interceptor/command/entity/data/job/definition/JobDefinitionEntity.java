package com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-12 10:42
 */
public interface JobDefinitionEntity extends JobDefinition {
    void setId(int id);

    void setAppId(int appId);

    void setJobName(String jobName);

    void setJobDesc(String jobDesc);

    void setCron(String cron);

    void setTaskType(int taskType);

    void setEnable(Boolean enable);

    void setPostDefinitionID(String postDefinitionID);

}
