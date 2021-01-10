package com.sumu.jobserver.scheduler.core.service.impl;

import com.sumu.jobserver.scheduler.core.service.JobApplicationService;
import com.sumu.jobserver.scheduler.interceptor.command.Command;
import com.sumu.jobserver.scheduler.interceptor.command.cmd.worker.RegisterWorkerCommand;
import com.sumu.jobserver.scheduler.interceptor.command.cmd.worker.WorkerBuilder;
import com.sumu.jobserver.scheduler.interceptor.command.context.CommandContext;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.worker.Worker;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-08 09:26
 */
public class JobApplicationServiceImpl extends ServiceImpl implements JobApplicationService {

    @Override
    public WorkerBuilder createBuilder() {
        return this.commandExecutor.execute(new Command<WorkerBuilder>() {
            @Override
            public WorkerBuilder execute(CommandContext commandContext) {
                return new WorkerBuilder(JobApplicationServiceImpl.this);
            }
        });
    }

    public Worker registerWorker(WorkerBuilder worker) {
        Worker res = this.commandExecutor.execute(new RegisterWorkerCommand(worker));
        return res;
    }
}
