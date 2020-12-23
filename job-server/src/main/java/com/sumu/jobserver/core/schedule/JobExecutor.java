package com.sumu.jobserver.core.schedule;

import com.sumu.common.util.rpc.RpcAddress;
import com.sumu.common.util.rpc.feign.FeignUtil;
import com.sumu.jobserver.mapper.JobMapper;
import com.sumu.jobserver.mapper.WorkerMapper;
import com.sumu.jobserver.modal.job.JobDefinitionDO;
import com.sumu.jobserver.modal.job.JobInstanceDO;
import com.sumu.jobserver.modal.worker.WorkerDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 15:41
 */
@Component
public class JobExecutor {

    private Logger LOG = LoggerFactory.getLogger(JobExecutor.class);

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private WorkerMapper workerMapper;

    public void executorByQuartz(String jobDefinitionId, Date fireTime, Date scheduledFireTime) {
        //Job定义
        JobDefinitionDO jobDefinitionDO = jobMapper.getJobDefinitionByID(jobDefinitionId);
        if (!jobDefinitionDO.getEnable()) {
            LOG.info("[Job Scheduler] Job closed ");
            return;
        }
        int appId = jobDefinitionDO.getAppId();
        String handlerName = jobDefinitionDO.getHandlerName();
        int strategy = jobDefinitionDO.getStrategy();
        //获取APPID注册的机器
        List<WorkerDO> workers = workerMapper.getRunWorkerByAppID(appId);
        //Job实例
        JobInstanceDO jobInstanceDO = new JobInstanceDO();
        jobInstanceDO.setJobDefinitionId(Integer.valueOf(jobDefinitionId));
        jobInstanceDO.setStartTime(new Date());
        jobInstanceDO.setTriggerType(1);//1-自动 0-手动
        String workersIP = "";
        //todo:调度
        switch (strategy) {
            case 1://默认
                workersIP = defaultStrategy(workers, handlerName);
                break;
            case 2://集群
                break;
            case 3://分片
                break;
            default:
                break;
        }
        jobInstanceDO.setTriggerWorker(workersIP);//调度的机器地址
        jobInstanceDO.setTriggerResult(1);
        //
        jobInstanceDO.setEndTime(new Date());
        jobMapper.insertJobInstance(jobInstanceDO);
        LOG.info("执行Job,{},{},{}", jobDefinitionId, fireTime, scheduledFireTime);
    }

    private String defaultStrategy(List<WorkerDO> workers, String handlerName) {
        StringBuilder sb = new StringBuilder();
        for (WorkerDO workerDO : workers) {
            RpcAddress rpcAddress = new RpcAddress(workerDO.getIp(), workerDO.getPort());
            sb.append(rpcAddress.getRpcAddress());
            FeignUtil.jobNotify(rpcAddress, handlerName);
            LOG.info("RpcAddress,{},handlerName,{}", rpcAddress.getRpcAddress(), handlerName);
        }

        return sb.toString();
    }
}
