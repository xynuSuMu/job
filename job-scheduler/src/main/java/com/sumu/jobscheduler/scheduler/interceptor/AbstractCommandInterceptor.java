package com.sumu.jobscheduler.scheduler.interceptor;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-08 15:23
 */
public abstract class AbstractCommandInterceptor implements CommandInterceptor {

    protected CommandInterceptor next;

    public AbstractCommandInterceptor() {
    }

    public CommandInterceptor getNext() {
        return this.next;
    }

    public void setNext(CommandInterceptor next) {
        this.next = next;
    }

}
