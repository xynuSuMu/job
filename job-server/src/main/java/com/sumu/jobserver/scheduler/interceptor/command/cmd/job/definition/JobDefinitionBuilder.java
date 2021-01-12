package com.sumu.jobserver.scheduler.interceptor.command.cmd.job.definition;

import com.sumu.jobserver.scheduler.core.service.impl.JobDefinitionServiceImpl;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.definition.JobDefinition;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.definition.JobDefinitionEntity;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.definition.JobDefinitionEntityImpl;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-12 10:31
 */
public class JobDefinitionBuilder {

    private JobDefinitionEntity jobDefinitionEntity;

    private JobDefinitionServiceImpl jobDefinitionService;

    public JobDefinitionBuilder(JobDefinitionServiceImpl jobDefinitionService) {
        this.jobDefinitionService = jobDefinitionService;
        jobDefinitionEntity = new JobDefinitionEntityImpl();
    }

    public JobDefinitionBuilder id(int id) {
        jobDefinitionEntity.setId(id);
        return this;
    }

    public JobDefinitionBuilder appId(int appId) {
        jobDefinitionEntity.setAppId(appId);
        return this;
    }

    public JobDefinitionBuilder taskType(int taskType) {
        jobDefinitionEntity.setTaskType(taskType);
        return this;
    }

    public JobDefinitionBuilder jobName(String jobName) {
        jobDefinitionEntity.setJobName(jobName);
        return this;
    }

    public JobDefinitionBuilder jobDesc(String jobDesc) {
        jobDefinitionEntity.setJobDesc(jobDesc);
        return this;
    }

    public JobDefinitionBuilder cron(String cron) {
        jobDefinitionEntity.setCron(cron);
        return this;
    }


    public JobDefinitionBuilder postDefinitionID(String postDefinitionID) {
        jobDefinitionEntity.setPostDefinitionID(postDefinitionID);
        return this;
    }


    public JobDefinitionBuilder enable(Boolean enable) {
        jobDefinitionEntity.setEnable(enable);
        return this;
    }

    JobDefinitionEntity getJobDefinitionEntity() {
        return jobDefinitionEntity;
    }

    public JobDefinition deploy() {
        return jobDefinitionService.create(this);
    }

    public Boolean delete() {
        return jobDefinitionService.deleteJobDefinition(this);
    }
}
