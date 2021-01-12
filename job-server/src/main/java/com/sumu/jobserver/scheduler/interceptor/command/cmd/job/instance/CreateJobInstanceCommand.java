package com.sumu.jobserver.scheduler.interceptor.command.cmd.job.instance;

import com.sumu.jobserver.scheduler.interceptor.command.Command;
import com.sumu.jobserver.scheduler.interceptor.command.context.CommandContext;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.instance.JobInstance;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.instance.JobInstanceEntity;
import com.sumu.jobserver.scheduler.mapper.JobMapper;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-12 16:20
 */
public class CreateJobInstanceCommand implements Command<JobInstance> {

    private JobInstanceBuilder jobInstanceBuilder;

    public CreateJobInstanceCommand(JobInstanceBuilder jobInstanceBuilder) {
        this.jobInstanceBuilder = jobInstanceBuilder;
    }

    @Override
    public JobInstance execute(CommandContext commandContext) {
        JobMapper jobMapper = commandContext.getSqlSession().getMapper(JobMapper.class);
        JobInstanceEntity jobInstanceEntity = jobInstanceBuilder.getJobInstanceEntity();
        if (jobInstanceEntity.getId() > 0) {
            jobMapper.updateJobInstance(jobInstanceEntity);
        } else {
            jobMapper.insertJobInstance(jobInstanceEntity);
        }
        return jobInstanceEntity;
    }
}
