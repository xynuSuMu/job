package com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.definition.shell;

import com.sumu.jobscheduler.scheduler.interceptor.command.Command;
import com.sumu.jobscheduler.scheduler.interceptor.command.context.CommandContext;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.shell.ShellJobDefinition;
import com.sumu.jobscheduler.scheduler.mapper.JobMapper;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-19 16:23
 */
public class ShellJobDefinitionQueryCommand implements Command<ShellJobDefinition> {

    private ShellJobQuery shellJobQuery;

    public ShellJobDefinitionQueryCommand(ShellJobQuery shellJobQuery) {
        this.shellJobQuery = shellJobQuery;
    }

    @Override
    public ShellJobDefinition execute(CommandContext commandContext) {
        JobMapper jobMapper = commandContext.getSqlSession().getMapper(JobMapper.class);
        ShellJobDefinition shellJobDefinition = jobMapper.getShellJobDefinition(shellJobQuery.getShellJobDefinitionEntity());
        return shellJobDefinition;
    }
}
