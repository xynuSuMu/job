package com.sumu.jobscheduler.scheduler.interceptor;

import com.sumu.jobscheduler.scheduler.config.auto.SpringJobConfiguration;
import com.sumu.jobscheduler.scheduler.context.JobApplicationContext;
import com.sumu.jobscheduler.scheduler.interceptor.command.Command;
import com.sumu.jobscheduler.scheduler.interceptor.command.context.CommandContext;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-08 15:36
 */
public class CommandContextInterceptor extends AbstractCommandInterceptor {

    private SpringJobConfiguration springJobConfiguration;

    public CommandContextInterceptor(SpringJobConfiguration springJobConfiguration) {
        this.springJobConfiguration = springJobConfiguration;
    }

    @Override
    public <T> T execute(Command<T> command) {
        CommandContext commandContext = JobApplicationContext.getCommandContext();
        if (commandContext == null) {
            commandContext = new CommandContext(command, springJobConfiguration);
        }
        try {
            JobApplicationContext.setCommandContext(commandContext);
            Object object = this.next.execute(command);
            return (T) object;
        } catch (Exception e) {
            commandContext.setException(e);
        } finally {
            try {
                commandContext.close();
            } finally {
                JobApplicationContext.removeCommandContext();
            }
        }
        return null;
    }
}
