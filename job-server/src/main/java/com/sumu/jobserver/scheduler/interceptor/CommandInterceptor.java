package com.sumu.jobserver.scheduler.interceptor;

import com.sumu.jobserver.scheduler.interceptor.command.Command;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-08 11:21
 */
public interface CommandInterceptor {

    <T> T execute(Command<T> command);

    CommandInterceptor getNext();

    void setNext(CommandInterceptor next);
}
