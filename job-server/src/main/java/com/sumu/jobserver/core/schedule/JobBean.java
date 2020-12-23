package com.sumu.jobserver.core.schedule;

import com.sumu.jobserver.util.SpringContextUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 15:39
 */
public class JobBean extends QuartzJobBean {

    private Logger LOG = LoggerFactory.getLogger(JobBean.class);


    @Override
    protected void executeInternal(JobExecutionContext ctx) {
        LOG.trace("JobExecutionContext: {}", ctx);

        JobKey jobKey = ctx.getTrigger().getJobKey();

        String jobDefinitionId = jobKey.getName();

        LOG.info("[JobDefinitionId={}] [trigger] submit trigger job to thread pool ", jobDefinitionId);
        JobExecutor jobExecutor = SpringContextUtils.getBean(JobExecutor.class);
        jobExecutor.executorByQuartz(jobDefinitionId, ctx.getFireTime(), ctx.getScheduledFireTime());
    }


}
