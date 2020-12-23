package com.sumu.jobserver.core.monitor;

import com.sumu.jobserver.mapper.AppMapper;
import com.sumu.jobserver.mapper.WorkerMapper;
import com.sumu.jobserver.modal.app.AppDO;
import com.sumu.jobserver.modal.zk.ZkDataModal;
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

    public void register(ZkDataModal zkDataModal, long zxID) {
        //APP应用注册
        appMapper.insertAppCode(zkDataModal.getAppName(), zxID);
        AppDO appDO = appMapper.getByAppName(zkDataModal.getAppName());
        //机器注册
        workerMapper.registerWorker(appDO.getId(),
                zkDataModal.getHostName(),
                zkDataModal.getIp(),
                zkDataModal.getPort(),
                zxID);
    }

    public void unregister(ZkDataModal zkDataModal, long zxID) {
        workerMapper.unRegisterWorker(zkDataModal.getIp(), zkDataModal.getPort(), zxID);
    }

}
