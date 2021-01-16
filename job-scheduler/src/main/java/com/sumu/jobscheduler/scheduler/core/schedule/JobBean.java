package com.sumu.jobscheduler.scheduler.core.schedule;

import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 15:39
 * @desc 该Bean目录结构发生改表时，历史Job需要需要更改QRTZ_JOB_DETAILS表JOB_CLASS_NAME字段
 * */
public class JobBean extends QuartzJobBean {


    @Autowired
    private JobDispatcher jobDispatcher;

    @Override
    protected void executeInternal(JobExecutionContext ctx) {
        JobKey jobKey = ctx.getTrigger().getJobKey();
        String jobDefinitionId = jobKey.getName();
        jobDispatcher.schedule(jobDefinitionId);
    }


}
