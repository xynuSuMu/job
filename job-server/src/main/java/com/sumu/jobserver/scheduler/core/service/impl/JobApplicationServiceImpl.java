package com.sumu.jobserver.scheduler.core.service.impl;

import com.sumu.jobserver.scheduler.context.JobApplicationContext;
import com.sumu.jobserver.scheduler.core.service.JobApplicationService;
import com.sumu.jobserver.scheduler.interceptor.command.Command;
import com.sumu.jobserver.scheduler.interceptor.command.entity.worker.RegisterWorkerCommand;
import com.sumu.jobserver.scheduler.mapper.WorkerMapper;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-08 09:26
 */
public class JobApplicationServiceImpl extends ServiceImpl implements JobApplicationService {

    @Override
    public void registerWorker() {
        Boolean res = this.commandExecutor.execute(new RegisterWorkerCommand());
        System.out.println(res);
    }
}
