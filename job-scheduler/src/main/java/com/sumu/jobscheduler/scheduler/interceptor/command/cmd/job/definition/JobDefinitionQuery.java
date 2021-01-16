package com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.definition;

import com.sumu.jobscheduler.scheduler.core.service.impl.JobDefinitionServiceImpl;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.JobDefinition;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.JobDefinitionEntity;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.JobDefinitionEntityImpl;

import java.util.List;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-12 11:11
 */
public class JobDefinitionQuery {

    private JobDefinitionEntity jobDefinitionEntity;

    private JobDefinitionServiceImpl jobDefinitionService;

    private int pageIndex = 1;
    private int pageSize = 10;

    public JobDefinitionQuery(JobDefinitionServiceImpl jobDefinitionService) {
        this.jobDefinitionService = jobDefinitionService;
        this.jobDefinitionEntity = new JobDefinitionEntityImpl();
    }


    public JobDefinitionQuery id(int id) {
        jobDefinitionEntity.setId(id);
        return this;
    }

    public JobDefinitionQuery appId(int appId) {
        jobDefinitionEntity.setAppId(appId);
        return this;
    }

    public JobDefinitionQuery taskType(int taskType) {
        jobDefinitionEntity.setTaskType(taskType);
        return this;
    }

    public JobDefinitionQuery jobName(String jobName) {
        jobDefinitionEntity.setJobName(jobName);
        return this;
    }

    public JobDefinitionQuery jobDesc(String jobDesc) {
        jobDefinitionEntity.setJobDesc(jobDesc);
        return this;
    }

    public JobDefinitionQuery cron(String cron) {
        jobDefinitionEntity.setCron(cron);
        return this;
    }


    public JobDefinitionQuery postDefinitionID(String postDefinitionID) {
        jobDefinitionEntity.setPostDefinitionID(postDefinitionID);
        return this;
    }


    public JobDefinitionQuery enable(Boolean enable) {
        jobDefinitionEntity.setEnable(enable);
        return this;
    }

    public JobDefinitionQuery index(int pageIndex) {
        this.pageIndex = pageIndex;
        return this;
    }

    public JobDefinitionQuery pageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    int getPageIndex() {
        return pageIndex;
    }

    int getPageSize() {
        return pageSize;
    }

    JobDefinitionEntity getJobDefinitionEntity() {
        return jobDefinitionEntity;
    }


    public List<JobDefinition> list() {
        return jobDefinitionService.list(this);
    }

    public JobDefinition singleResult() {
        List<JobDefinition> list = list();
        if (list == null || list.size() == 0)
            return null;
        return list.get(0);
    }

    public int count() {
        return jobDefinitionService.count(this);
    }
}
