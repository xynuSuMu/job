package com.sumu.jobserver.core.schedule.java;

import com.sumu.common.util.rpc.RpcAddress;
import com.sumu.common.util.rpc.RpcResult;
import com.sumu.common.util.rpc.feign.FeignUtil;
import com.sumu.jobserver.core.schedule.AbstractJobExecutor;
import com.sumu.jobserver.mapper.JobMapper;
import com.sumu.jobserver.mapper.WorkerMapper;
import com.sumu.jobserver.modal.job.JavaJobDO;
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
public class JobExecutor extends AbstractJobExecutor {

    private Logger LOG = LoggerFactory.getLogger(JobExecutor.class);

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private WorkerMapper workerMapper;

    @Override
    public void executorByQuartz(JobDefinitionDO jobDefinitionDO) {
        int jobDefinitionId = jobDefinitionDO.getId();
        int appId = jobDefinitionDO.getAppId();
        //
        JavaJobDO javaJobDO = jobMapper.getJavaJobDefinitionByDefId(jobDefinitionId);
        String handlerName = javaJobDO.getHandlerName();
        int strategy = javaJobDO.getStrategy();
        //获取APPID注册的机器
        List<WorkerDO> workers = workerMapper.getRunWorkerByAppID(appId);
        //Job实例
        JobInstanceDO jobInstanceDO = new JobInstanceDO();
        jobInstanceDO.setJobDefinitionId(jobDefinitionId);
        jobInstanceDO.setStartTime(new Date());
        jobInstanceDO.setTriggerType(1);//1-自动 0-手动
        //todo:调度
        switch (strategy) {
            case 1://默认
                defaultStrategy(workers, handlerName, jobInstanceDO);
                break;
            case 2://集群
                break;
            case 3://分片
                break;
            default:
                break;
        }
        //
        jobInstanceDO.setEndTime(new Date());
        jobMapper.insertJobInstance(jobInstanceDO);
        LOG.info("执行Job,jobDefinitionId = {}", jobDefinitionId);
    }

    private void defaultStrategy(List<WorkerDO> workers, String handlerName, JobInstanceDO jobInstanceDO) {
        StringBuilder sb = new StringBuilder();
        for (WorkerDO workerDO : workers) {
            RpcAddress rpcAddress = new RpcAddress(workerDO.getIp(), workerDO.getPort());
            sb.append(rpcAddress.getRpcAddress());
            LOG.info("RpcAddress,{},handlerName,{}", rpcAddress.getRpcAddress(), handlerName);
            RpcResult<Void> rpcResult = FeignUtil.jobNotify(rpcAddress, handlerName);
            if (!rpcResult.isSuccess()) {
                LOG.error("Error , {}", rpcResult.getMsg());
            }
            jobInstanceDO.setTriggerResult(rpcResult.isSuccess() ? 1 : 0);
            break;
        }
        jobInstanceDO.setTriggerWorker(sb.toString());

    }
}
