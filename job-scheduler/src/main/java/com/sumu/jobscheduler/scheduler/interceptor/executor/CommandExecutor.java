package com.sumu.jobscheduler.scheduler.interceptor.executor;

import com.sumu.jobscheduler.scheduler.interceptor.command.Command;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-08 10:37
 */
public interface CommandExecutor {

    <T> T execute(Command<T> command);

}
