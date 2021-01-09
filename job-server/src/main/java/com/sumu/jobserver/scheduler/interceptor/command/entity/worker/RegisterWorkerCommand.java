package com.sumu.jobserver.scheduler.interceptor.command.entity.worker;

import com.sumu.jobserver.scheduler.interceptor.command.Command;
import com.sumu.jobserver.scheduler.interceptor.command.context.CommandContext;
import com.sumu.jobserver.scheduler.mapper.WorkerMapper;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-09 15:56
 */
public class RegisterWorkerCommand implements Command<Boolean> {
    @Override
    public Boolean execute(CommandContext commandContext) {
        WorkerMapper workerMapper = commandContext.getSqlSession().getMapper(WorkerMapper.class);

//        //机器注册
        workerMapper.registerWorker(111,
                "111",
                "111",
                111,
                111);
//        throw new RuntimeException("1111");
        return true;
    }
}
