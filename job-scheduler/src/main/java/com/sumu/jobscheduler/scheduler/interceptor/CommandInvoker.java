package com.sumu.jobscheduler.scheduler.interceptor;

import com.sumu.jobscheduler.scheduler.context.JobApplicationContext;
import com.sumu.jobscheduler.scheduler.interceptor.command.Command;
import com.sumu.jobscheduler.scheduler.interceptor.command.context.CommandContext;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-08 15:35
 */
public class CommandInvoker extends AbstractCommandInterceptor {

    @Override
    public <T> T execute(Command<T> command) {
        return doInvoke(command);
    }

    private <T> T doInvoke(Command<T> command) {
        CommandContext commandContext = JobApplicationContext.getCommandContext();
        commandContext.setResult(command.execute(commandContext));
        return (T) commandContext.getResult();
    }

}
