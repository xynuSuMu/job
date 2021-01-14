package com.sumu.jobserver.scheduler.core.schedule;

import com.sumu.jobserver.scheduler.core.service.JobDefinitionService;
import com.sumu.jobserver.scheduler.core.service.impl.JobDefinitionServiceImpl;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.definition.JobDefinition;
import com.sumu.jobserver.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-24 18:14
 */
public abstract class AbstractJobExecutor {

    private Logger LOG = LoggerFactory.getLogger(AbstractJobExecutor.class);

    private JobDefinition prepareJobDefinition(String jobDefinitionId) {
        JobDefinitionService jobDefinitionService =
                SpringContextUtils.getBean(JobDefinitionServiceImpl.class);
        JobDefinition jobDefinition =
                jobDefinitionService.createQuery()
                        .id(Integer.valueOf(jobDefinitionId))
                        .singleResult();
        return jobDefinition;
    }

    public void executor(String jobDefinitionId) {
        JobDefinition jobDefinitionDO = this.prepareJobDefinition(jobDefinitionId);
        executorByQuartz(jobDefinitionDO);
        finish(jobDefinitionDO);
    }

    public abstract void executorByQuartz(JobDefinition jobDefinitionDO);

    public void finish(JobDefinition jobDefinitionDO) {
        LOG.info("[ Job Schedule Finish ] jobName={},cron={}", jobDefinitionDO.getJobName(), jobDefinitionDO.getCron());
    }

}
