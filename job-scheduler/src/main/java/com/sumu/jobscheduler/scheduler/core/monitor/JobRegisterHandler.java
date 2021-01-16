package com.sumu.jobscheduler.scheduler.core.monitor;

import com.sumu.jobscheduler.scheduler.core.service.JobApplicationService;
import com.sumu.jobscheduler.scheduler.core.service.WorkerService;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.app.App;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.worker.Worker;
import com.sumu.jobscheduler.scheduler.modal.zk.ZkDataModal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 17:00
 */
public class JobRegisterHandler {

    private JobApplicationService jobApplicationService;

    private WorkerService workerService;

    public JobRegisterHandler(JobApplicationService jobApplicationService, WorkerService workerService) {
        this.jobApplicationService = jobApplicationService;
        this.workerService = workerService;
    }

    public Worker register(ZkDataModal zkDataModal, long zxID) {

        //应用创建->查询
        App app = jobApplicationService.createAppBuilder()
                .appCode(zkDataModal.getAppName())
                .zxID(zxID)
                .create();

        //机器注册
        Worker worker = workerService.createBuilder()
                .appId(app.getID())
                .ip(zkDataModal.getIp())
                .port(zkDataModal.getPort())
                .hostName(zkDataModal.getHostName())
                .zxID(zxID)
                .registerWorker();
        return worker;
    }

    public void unregister(ZkDataModal zkDataModal, long zxID) {
        workerService.createBuilder()
                .ip(zkDataModal.getIp())
                .port(zkDataModal.getPort())
                .zxID(zxID)
                .unRegisterWorker();
    }

}
