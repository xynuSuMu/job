package com.sumu.jobserver.scheduler.interceptor.command.cmd.job.definition;

import com.sumu.jobserver.scheduler.interceptor.command.Command;
import com.sumu.jobserver.scheduler.interceptor.command.context.CommandContext;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.definition.JobDefinition;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.definition.JobDefinitionEntity;
import com.sumu.jobserver.scheduler.mapper.JobMapper;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-12 10:50
 */
public class CreateJobDefinitionCommand implements Command<JobDefinition> {

    private JobDefinitionEntity jobDefinition;

    public CreateJobDefinitionCommand(JobDefinitionBuilder jobDefinitionBuilder) {
        this.jobDefinition = jobDefinitionBuilder.getJobDefinitionEntity();
    }

    @Override
    public JobDefinition execute(CommandContext commandContext) {
        JobMapper jobMapper = commandContext.getSqlSession().getMapper(JobMapper.class);
        if (jobDefinition.getId() > 0) {
            jobMapper.updateJobDefinition(jobDefinition);
        } else {
            jobMapper.insertJobDefinition(jobDefinition);
        }
        return jobDefinition;
    }

}

