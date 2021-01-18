package com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-12 10:43
 */
public interface JobDefinition {

    int getId();

    int getAppId();

    String getJobName();

    String getJobDesc();

    String getCron();

    int getTaskType();

    Boolean getEnable();

    String getPostDefinitionID();
}

