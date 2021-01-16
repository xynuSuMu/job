package com.sumu.jobscheduler.scheduler.interceptor.command.cmd.worker;

import com.sumu.jobscheduler.scheduler.core.service.impl.WorkerServiceImpl;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.worker.Worker;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.worker.WorkerEntity;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.worker.WorkerEntityImpl;

import java.util.List;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-11 15:09
 */
public class WorkerQuery {

    private WorkerEntity workerEntity;

    private WorkerServiceImpl workerService;

    public WorkerQuery(WorkerServiceImpl workerService) {
        this.workerService = workerService;
        workerEntity = new WorkerEntityImpl();
    }


    public WorkerQuery appId(int appID) {
        workerEntity.setAppID(appID);
        return this;
    }

    public WorkerQuery enable(Boolean enable) {
        workerEntity.setEnable(enable);
        return this;
    }

    public WorkerQuery hostName(String hostName) {
        workerEntity.setHostName(hostName);
        return this;
    }

    public WorkerEntity getWorkerEntity() {
        return workerEntity;
    }

    public List<Worker> list() {
        return workerService.query(this);
    }
}
