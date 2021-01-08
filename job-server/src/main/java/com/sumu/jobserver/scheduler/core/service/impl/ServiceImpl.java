package com.sumu.jobserver.scheduler.core.service.impl;

import com.sumu.jobserver.scheduler.interceptor.executor.CommandExecutor;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-08 09:27
 */
public class ServiceImpl {

    protected CommandExecutor commandExecutor;

    public void setCommandExecutor(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

}
