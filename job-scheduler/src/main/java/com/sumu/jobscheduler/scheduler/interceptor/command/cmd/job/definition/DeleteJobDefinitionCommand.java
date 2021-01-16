package com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.definition;

import com.sumu.jobscheduler.scheduler.interceptor.command.Command;
import com.sumu.jobscheduler.scheduler.interceptor.command.context.CommandContext;
import com.sumu.jobscheduler.scheduler.mapper.JobMapper;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-12 16:00
 */
public class DeleteJobDefinitionCommand implements Command<Boolean> {

    private JobDefinitionBuilder jobDefinitionBuilder;

    public DeleteJobDefinitionCommand(JobDefinitionBuilder jobDefinitionBuilder) {
        this.jobDefinitionBuilder = jobDefinitionBuilder;
    }

    @Override
    public Boolean execute(CommandContext commandContext) {
        int id = jobDefinitionBuilder.getJobDefinitionEntity().getId();
        if (!(id > 0)) {
            return false;
        }
        JobMapper jobMapper = commandContext.getSqlSession().getMapper(JobMapper.class);
        jobMapper.removeJobDefinition(id);
        return true;
    }
}
