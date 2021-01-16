package com.sumu.jobscheduler.scheduler.interceptor.command.cmd.worker;

import com.sumu.jobscheduler.scheduler.core.service.impl.WorkerServiceImpl;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.worker.Worker;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.worker.WorkerEntity;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.worker.WorkerEntityImpl;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-10 20:28
 */
public class WorkerBuilder {

    private WorkerEntity workerEntity;

    private WorkerServiceImpl workerService;

    private Boolean register = true;


    public WorkerBuilder(WorkerServiceImpl workerService) {
        this.workerService = workerService;
        this.workerEntity = new WorkerEntityImpl();
    }

    public WorkerBuilder appId(int appID) {
        workerEntity.setAppID(appID);
        return this;
    }

    public WorkerBuilder hostName(String hostName) {
        workerEntity.setHostName(hostName);
        return this;
    }

    public WorkerBuilder ip(String ip) {
        workerEntity.setIp(ip);
        return this;
    }

    public WorkerBuilder port(int port) {
        workerEntity.setPort(port);
        return this;
    }

    public WorkerBuilder zxID(long zxID) {
        workerEntity.setZxID(zxID);
        return this;
    }

    public Boolean getRegister() {
        return register;
    }

    public WorkerEntity getWorkerEntity() {
        return workerEntity;
    }

    public Worker registerWorker() {
        return this.workerService.registerWorker(this);
    }

    public Worker unRegisterWorker() {
        this.register = false;
        return this.workerService.unRegisterWorker(this);
    }
}
