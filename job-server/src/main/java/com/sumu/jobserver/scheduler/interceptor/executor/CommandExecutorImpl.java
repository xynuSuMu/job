package com.sumu.jobserver.scheduler.interceptor.executor;

import com.sumu.jobserver.scheduler.interceptor.CommandInterceptor;
import com.sumu.jobserver.scheduler.interceptor.command.Command;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-08 15:56
 */
public class CommandExecutorImpl implements CommandExecutor {

    private CommandInterceptor first;

    public CommandExecutorImpl(CommandInterceptor first) {
        this.first = first;
    }

    @Override
    public <T> T execute(Command<T> command) {
        return this.first.execute(command);
    }
}
