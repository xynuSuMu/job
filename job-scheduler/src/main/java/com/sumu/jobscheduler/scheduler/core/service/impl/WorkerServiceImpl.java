package com.sumu.jobscheduler.scheduler.core.service.impl;

import com.sumu.jobscheduler.scheduler.core.service.WorkerService;
import com.sumu.jobscheduler.scheduler.interceptor.command.Command;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.worker.QueryWorkerCommand;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.worker.RegisterWorkerCommand;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.worker.WorkerBuilder;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.worker.WorkerQuery;
import com.sumu.jobscheduler.scheduler.interceptor.command.context.CommandContext;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.worker.Worker;

import java.util.List;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-11 10:54
 */
public class WorkerServiceImpl extends ServiceImpl implements WorkerService {
    @Override
    public WorkerBuilder createBuilder() {
        return this.commandExecutor.execute(new Command<WorkerBuilder>() {
            @Override
            public WorkerBuilder execute(CommandContext commandContext) {
                return new WorkerBuilder(WorkerServiceImpl.this);
            }
        });
    }

    @Override
    public WorkerQuery createQuery() {
        return this.commandExecutor.execute(new Command<WorkerQuery>() {
            @Override
            public WorkerQuery execute(CommandContext commandContext) {
                return new WorkerQuery(WorkerServiceImpl.this);
            }
        });
    }

    public Worker registerWorker(WorkerBuilder worker) {
        Worker res = this.commandExecutor.execute(new RegisterWorkerCommand(worker));
        return res;
    }

    public Worker unRegisterWorker(WorkerBuilder worker) {
        Worker res = this.commandExecutor.execute(new RegisterWorkerCommand(worker));
        return res;
    }

    public List<Worker> query(WorkerQuery workerQuery) {
        return this.commandExecutor.execute(new QueryWorkerCommand(workerQuery));
    }
}
