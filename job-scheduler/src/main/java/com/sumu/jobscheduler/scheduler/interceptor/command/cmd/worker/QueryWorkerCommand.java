package com.sumu.jobscheduler.scheduler.interceptor.command.cmd.worker;

import com.sumu.jobscheduler.scheduler.interceptor.command.Command;
import com.sumu.jobscheduler.scheduler.interceptor.command.context.CommandContext;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.worker.Worker;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.worker.WorkerEntityImpl;
import com.sumu.jobscheduler.scheduler.mapper.WorkerMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-11 15:12
 */
public class QueryWorkerCommand implements Command<List<Worker>> {

    private WorkerQuery workerQuery;

    public QueryWorkerCommand(WorkerQuery workerQuery) {
        this.workerQuery = workerQuery;
    }

    @Override
    public List<Worker> execute(CommandContext commandContext) {
        WorkerMapper workerMapper = commandContext.getSqlSession().getMapper(WorkerMapper.class);
        List<WorkerEntityImpl> workerEntities = workerMapper.getJobWorker(workerQuery.getWorkerEntity());
        List<Worker> res = new ArrayList<>();
        workerEntities.stream().forEach(workerEntity -> {
            res.add(workerEntity);
        });
        return res;
    }

}
