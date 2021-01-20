package com.sumu.jobscheduler.scheduler.core.schedule;

import com.sumu.jobscheduler.scheduler.core.service.JobDefinitionService;
import com.sumu.jobscheduler.scheduler.core.service.JobInstanceService;
import com.sumu.jobscheduler.scheduler.core.service.impl.JobDefinitionServiceImpl;
import com.sumu.jobscheduler.scheduler.core.service.impl.JobInstanceServiceImpl;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.JobDefinition;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.instance.JobInstance;
import com.sumu.jobscheduler.scheduler.modal.job.ExecutorResult;
import com.sumu.jobscheduler.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

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

    private JobInstance prepareJobInstance(Integer jobDefinitionId) {
        JobInstanceService jobInstanceService =
                SpringContextUtils.getBean(JobInstanceServiceImpl.class);
        //Job实例
        JobInstance jobInstanceDO = jobInstanceService.createBuilder()
                .jobDefinitionId(jobDefinitionId)
                .startTime(new Date())
                .triggerType(1)
                .create();
        return jobInstanceDO;
    }

    public void executor(String jobDefinitionId) {
        JobDefinition jobDefinitionDO = this.prepareJobDefinition(jobDefinitionId);
        JobInstance jobInstance = prepareJobInstance(Integer.valueOf(jobDefinitionId));
        executorByQuartz(jobDefinitionDO, jobInstance);
        finish(jobDefinitionDO);
    }

    public abstract void executorByQuartz(JobDefinition jobDefinitionDO, JobInstance jobInstance);


    protected void updateInstance(int instanceId, ExecutorResult result) {
        JobInstanceService jobInstanceService =
                SpringContextUtils.getBean(JobInstanceServiceImpl.class);
        jobInstanceService.createBuilder()
                .id(instanceId)
                .endTime(result.getEndTime())
                .triggerResult(result.getResult())
                .triggerWorker(result.getWorker())
                .create();
    }

    public void finish(JobDefinition jobDefinitionDO) {
        LOG.info("[ Job Schedule Finish ] jobName={},cron={}", jobDefinitionDO.getJobName(), jobDefinitionDO.getCron());
    }

}
