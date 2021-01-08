package com.sumu.jobserver.scheduler.interceptor.command;

import com.sumu.jobserver.scheduler.interceptor.command.context.CommandContext;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-08 10:38
 */
public interface Command<T> {

    T execute(CommandContext commandContext);

}
