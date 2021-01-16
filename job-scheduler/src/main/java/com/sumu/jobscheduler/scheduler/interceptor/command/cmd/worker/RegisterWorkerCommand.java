package com.sumu.jobscheduler.scheduler.interceptor.command.cmd.worker;

import com.sumu.jobscheduler.scheduler.interceptor.command.Command;
import com.sumu.jobscheduler.scheduler.interceptor.command.context.CommandContext;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.worker.Worker;
import com.sumu.jobscheduler.scheduler.mapper.WorkerMapper;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-09 15:56
 */
public class RegisterWorkerCommand implements Command<Worker> {

    private WorkerBuilder workerBuilder;

    public RegisterWorkerCommand(WorkerBuilder workerBuilder) {
        this.workerBuilder = workerBuilder;
    }

    @Override
    public Worker execute(CommandContext commandContext) {
        //todo:workMapper应该被管理
        WorkerMapper workerMapper = commandContext.getSqlSession()
                .getMapper(WorkerMapper.class);

        if (workerBuilder.getRegister()) {
            // 机器注册
            workerMapper.registerWorker(workerBuilder.getWorkerEntity());
        } else {
            // 机器下线
            workerMapper.unRegisterWorker(
                    workerBuilder.getWorkerEntity().getIp(),
                    workerBuilder.getWorkerEntity().getPort(),
                    workerBuilder.getWorkerEntity().getZxID()
            );
        }
        return workerBuilder.getWorkerEntity();
    }

}
