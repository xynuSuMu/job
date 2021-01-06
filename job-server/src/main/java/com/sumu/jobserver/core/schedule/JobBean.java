package com.sumu.jobserver.core.schedule;

import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 15:39
 */
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
