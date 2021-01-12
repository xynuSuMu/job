package com.sumu.jobserver.scheduler.interceptor.command.cmd.job.instance;

import com.sumu.jobserver.scheduler.core.service.impl.JobInstanceServiceImpl;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.instance.JobInstance;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.instance.JobInstanceEntity;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.instance.JobInstanceEntityImpl;

import java.util.Date;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-12 10:31
 */
public class JobInstanceBuilder {

    private JobInstanceEntity jobInstanceEntity;

    private JobInstanceServiceImpl jobInstanceService;

    public JobInstanceBuilder(JobInstanceServiceImpl jobInstanceService) {
        this.jobInstanceService = jobInstanceService;
        this.jobInstanceEntity = new JobInstanceEntityImpl();
    }

    public JobInstanceBuilder id(int id) {
        jobInstanceEntity.setId(id);
        return this;
    }

    public JobInstanceBuilder jobDefinitionId(int jobDefinitionId) {
        jobInstanceEntity.setJobDefinitionId(jobDefinitionId);
        return this;
    }

    public JobInstanceBuilder triggerResult(int triggerResult) {
        jobInstanceEntity.setTriggerResult(triggerResult);
        return this;
    }

    public JobInstanceBuilder triggerType(int triggerType) {
        jobInstanceEntity.setTriggerType(triggerType);
        return this;
    }

    public JobInstanceBuilder triggerWorker(String triggerWorker) {
        jobInstanceEntity.setTriggerWorker(triggerWorker);
        return this;
    }

    public JobInstanceBuilder startTime(Date startTime) {
        jobInstanceEntity.setStartTime(startTime);
        return this;
    }

    public JobInstanceBuilder endTime(Date endTime) {
        jobInstanceEntity.setEndTime(endTime);
        return this;
    }

     JobInstanceEntity getJobInstanceEntity() {
        return jobInstanceEntity;
    }

    public JobInstance create() {
        return jobInstanceService.create(this);
    }

}
