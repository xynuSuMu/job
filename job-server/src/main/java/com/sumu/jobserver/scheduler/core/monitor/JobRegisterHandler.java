package com.sumu.jobserver.scheduler.core.monitor;

import com.sumu.jobserver.scheduler.core.service.JobApplicationService;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.worker.Worker;
import com.sumu.jobserver.scheduler.mapper.AppMapper;
import com.sumu.jobserver.scheduler.mapper.WorkerMapper;
import com.sumu.jobserver.scheduler.modal.app.AppDO;
import com.sumu.jobserver.scheduler.modal.zk.ZkDataModal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 17:00
 */
@Component
public class JobRegisterHandler {

    @Autowired
    private AppMapper appMapper;

    @Autowired
    private WorkerMapper workerMapper;

    @Autowired
    private JobApplicationService jobApplicationService;

    public void register(ZkDataModal zkDataModal, long zxID) {
        //APP应用注册
        appMapper.insertAppCode(zkDataModal.getAppName(), zxID);
        AppDO appDO = appMapper.getByAppName(zkDataModal.getAppName());
        //机器注册
//        workerMapper.registerWorker(appDO.getId(),
//                zkDataModal.getHostName(),
//                zkDataModal.getIp(),
//                zkDataModal.getPort(),
//                zxID);
        Worker worker = jobApplicationService.createBuilder()
                .appId(appDO.getId())
                .ip(zkDataModal.getIp())
                .port(zkDataModal.getPort())
                .hostName(zkDataModal.getHostName())
                .zxID(zxID)
                .registerWorker();

    }

    public void unregister(ZkDataModal zkDataModal, long zxID) {
        workerMapper.unRegisterWorker(zkDataModal.getIp(), zkDataModal.getPort(), zxID);
    }

}
