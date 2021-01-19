package com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.definition.shell;

import com.sumu.jobscheduler.scheduler.interceptor.command.Command;
import com.sumu.jobscheduler.scheduler.interceptor.command.context.CommandContext;
import com.sumu.jobscheduler.scheduler.mapper.JobMapper;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-19 14:47
 */
public class CreateShellJobDefinitionCommand implements Command<Boolean> {

    private ShellJobBuilder shellJobBuilder;

    public CreateShellJobDefinitionCommand(ShellJobBuilder shellJobBuilder) {
        this.shellJobBuilder = shellJobBuilder;
    }

    @Override
    public Boolean execute(CommandContext commandContext) {
        JobMapper jobMapper = commandContext.getSqlSession().getMapper(JobMapper.class);
        jobMapper.insertShellJobDefinition(shellJobBuilder.getShellJobDefinitionEntity());
        return true;
    }
}
