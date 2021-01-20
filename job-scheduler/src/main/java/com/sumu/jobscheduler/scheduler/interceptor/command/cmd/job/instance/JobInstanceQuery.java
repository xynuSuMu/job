package com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.instance;

import com.sumu.jobscheduler.scheduler.core.service.impl.JobInstanceServiceImpl;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.instance.JobInstance;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.instance.JobInstanceEntity;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.instance.JobInstanceEntityImpl;

import java.util.Date;
import java.util.List;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-12 16:47
 */
public class JobInstanceQuery {

    private JobInstanceEntity jobInstanceEntity;

    private JobInstanceServiceImpl jobInstanceService;


    private int pageIndex = 1;

    private int pageSize = 10;

    public JobInstanceQuery(JobInstanceServiceImpl jobInstanceService) {
        this.jobInstanceService = jobInstanceService;
        this.jobInstanceEntity = new JobInstanceEntityImpl();
    }

    public JobInstanceQuery id(int id) {
        jobInstanceEntity.setId(id);
        return this;
    }

    public JobInstanceQuery jobDefinitionId(int jobDefinitionId) {
        jobInstanceEntity.setJobDefinitionId(jobDefinitionId);
        return this;
    }

    public JobInstanceQuery triggerResult(String triggerResult) {
        jobInstanceEntity.setTriggerResult(triggerResult);
        return this;
    }

    public JobInstanceQuery triggerType(int triggerType) {
        jobInstanceEntity.setTriggerType(triggerType);
        return this;
    }

    public JobInstanceQuery triggerWorker(String triggerWorker) {
        jobInstanceEntity.setTriggerWorker(triggerWorker);
        return this;
    }

    public JobInstanceQuery startTime(Date startTime) {
        jobInstanceEntity.setStartTime(startTime);
        return this;
    }

    public JobInstanceQuery endTime(Date endTime) {
        jobInstanceEntity.setEndTime(endTime);
        return this;
    }


    public JobInstanceQuery index(int pageIndex) {
        this.pageIndex = pageIndex;
        return this;
    }

    public JobInstanceQuery pageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    int getPageIndex() {
        return pageIndex;
    }

    int getPageSize() {
        return pageSize;
    }

    JobInstanceEntity getJobInstanceEntity() {
        return jobInstanceEntity;
    }

    public List<JobInstance> list() {
        return jobInstanceService.list(this);
    }

    public int count() {
        return jobInstanceService.count(this);
    }

}
