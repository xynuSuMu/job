package com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.instance;

import java.util.Date;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-12 16:10
 */
public interface JobInstance {

    int getId();

    int getJobDefinitionId();

    Date getStartTime();

    Date getEndTime();

    int getTriggerType();

    String getTriggerWorker();

    String getTriggerResult();
}
