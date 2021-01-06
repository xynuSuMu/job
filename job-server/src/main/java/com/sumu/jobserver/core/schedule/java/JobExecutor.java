package com.sumu.jobserver.core.schedule.java;

import com.sumu.common.util.rpc.RpcAddress;
import com.sumu.common.util.rpc.RpcResult;
import com.sumu.common.util.rpc.feign.FeignUtil;
import com.sumu.jobserver.core.schedule.AbstractJobExecutor;
import com.sumu.jobserver.core.schedule.JobDispatcher;
import com.sumu.jobserver.enume.JavaJobInfo;
import com.sumu.jobserver.enume.JobInfo;
import com.sumu.jobserver.mapper.JobMapper;
import com.sumu.jobserver.mapper.WorkerMapper;
import com.sumu.jobserver.modal.job.JavaJobDO;
import com.sumu.jobserver.modal.job.JobDefinitionDO;
import com.sumu.jobserver.modal.job.JobInstanceDO;
import com.sumu.jobserver.modal.worker.WorkerDO;
import com.sumu.jobserver.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

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

    @Autowired
    private JobDispatcher jobDispatcher;

    @Override
    public void executorByQuartz(JobDefinitionDO jobDefinitionDO) {
        int jobDefinitionId = jobDefinitionDO.getId();
        int appId = jobDefinitionDO.getAppId();
        //执行
        doExecute(appId, jobDefinitionId);
        //后置任务
        if (jobDefinitionDO.getPostDefinitionID() != null && !"".equals(jobDefinitionDO.getPostDefinitionID())) {
            String[] ids = jobDefinitionDO.getPostDefinitionID().split(",");
            for (String id : ids) {
                jobDispatcher.schedule(id);
            }
        }
        LOG.info("Job Execute Finish,jobDefinitionId = {}", jobDefinitionId);
    }

    private void doExecute(int appId, int jobDefinitionId) {
        JavaJobDO javaJobDO = jobMapper.getJavaJobDefinitionByDefId(jobDefinitionId);
        String handlerName = javaJobDO.getHandlerName();
        int strategy = javaJobDO.getStrategy();
        //获取AppID注册的机器
        List<WorkerDO> workers = workerMapper.getRunWorkerByAppID(appId);
        //Job实例
        JobInstanceDO jobInstanceDO = new JobInstanceDO();
        jobInstanceDO.setJobDefinitionId(jobDefinitionId);
        jobInstanceDO.setStartTime(new Date());
        jobInstanceDO.setTriggerType(1);//1-自动 0-手动
        jobMapper.insertJobInstance(jobInstanceDO);
        JavaJobInfo.Strategy schedulerStrategy = JavaJobInfo.Strategy.getStrategy(strategy);
        //调度
        switch (schedulerStrategy) {
            case DEFAULT:
                defaultStrategy(workers, handlerName, jobInstanceDO);
                break;
            case BROADCAST:
                clusterStrategy(workers, handlerName, jobInstanceDO);
                break;
            case SHARD:
                int total = javaJobDO.getShardNum();
                shardStrategy(total, workers, handlerName, jobInstanceDO);
                break;
            default:
                break;
        }
        jobInstanceDO.setEndTime(new Date());
        jobMapper.updateJobInstance(jobInstanceDO);
    }

    private void defaultStrategy(List<WorkerDO> workers, String handlerName, JobInstanceDO jobInstanceDO) {
        StringBuilder sb = new StringBuilder();
        jobInstanceDO.setTriggerResult(0);
        for (WorkerDO workerDO : workers) {
            RpcAddress rpcAddress = new RpcAddress(workerDO.getIp(), workerDO.getPort());
            sb.append(rpcAddress.getRpcAddress());
            LOG.info("RpcAddress,{},handlerName,{}", rpcAddress.getRpcAddress(), handlerName);
            RpcResult<Void> rpcResult = FeignUtil.jobNotify(rpcAddress,
                    handlerName,
                    String.valueOf(jobInstanceDO.getId()));
            if (!rpcResult.isSuccess()) {
                LOG.error("Error , {}", rpcResult.getMsg());
                continue;
            }
            jobInstanceDO.setTriggerResult(rpcResult.isSuccess() ? 1 : 0);
            break;
        }
        jobInstanceDO.setTriggerWorker(sb.toString());
    }

    private void clusterStrategy(List<WorkerDO> workers, String handlerName, JobInstanceDO jobInstanceDO) {
        StringBuilder sb = new StringBuilder();
        jobInstanceDO.setTriggerResult(0);
        for (WorkerDO workerDO : workers) {
            RpcAddress rpcAddress = new RpcAddress(workerDO.getIp(), workerDO.getPort());
            sb.append(rpcAddress.getRpcAddress());
            LOG.info("RpcAddress,{},handlerName,{}", rpcAddress.getRpcAddress(), handlerName);
            RpcResult<Void> rpcResult = FeignUtil.jobNotify(rpcAddress,
                    handlerName,
                    String.valueOf(jobInstanceDO.getId()));
            if (!rpcResult.isSuccess()) {
                LOG.error("Error , {}", rpcResult.getMsg());
            }
            jobInstanceDO.setTriggerResult(rpcResult.isSuccess() ? 1 : 0);
        }
        jobInstanceDO.setTriggerWorker(sb.toString());
    }

    //分片策略
    private void shardStrategy(int toatal, List<WorkerDO> workers, String handlerName, JobInstanceDO jobInstanceDO) {
        //
        Map<WorkerDO, List<Integer>> map = new HashMap<>();
        //分片索引
        int[] shard = new int[toatal];
        for (int i = 0; i < toatal; ++i) {
            shard[i] = i;
        }
        //对每个分片进行机器分配
        int size = workers.size();
        if (size == 0) {
            throw new RuntimeException("workers number must more than 0!");
        }
        if (toatal <= size) {
            for (int i = 0; i < size; ++i) {
                List<Integer> list = new ArrayList<>();
                list.add(i);
                map.put(workers.get(i), list);
            }
        } else {
            for (int i = 0; i < toatal; ++i) {
                if (i < size) {
                    List<Integer> list = new ArrayList<>();
                    list.add(i);
                    map.put(workers.get(i), list);
                } else {
                    int temp = i % size;
                    map.get(workers.get(temp)).add(i);
                }

            }
        }
        //
        Set<Map.Entry<WorkerDO, List<Integer>>> set = map.entrySet();
        StringBuilder sb = new StringBuilder();
        jobInstanceDO.setTriggerResult(1);
        for (Map.Entry<WorkerDO, List<Integer>> entry : set) {
            WorkerDO workerDO = entry.getKey();
            //分片索引
            List<Integer> shardIndex = entry.getValue();
            StringBuilder stringBuilder = new StringBuilder();
            for (Integer i : shardIndex) {
                stringBuilder.append(i + ",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            RpcAddress rpcAddress = new RpcAddress(workerDO.getIp(), workerDO.getPort());
            sb.append(rpcAddress.getRpcAddress());
            LOG.info("RpcAddress,{},handlerName,{}", rpcAddress.getRpcAddress(), handlerName);
            RpcResult<Void> rpcResult = FeignUtil.jobNotify(rpcAddress,
                    handlerName,
                    String.valueOf(jobInstanceDO.getId()),
                    stringBuilder.toString(),
                    String.valueOf(toatal));
            if (!rpcResult.isSuccess()) {
                LOG.error(" [Shard Error],address={},index ={},msg={}",
                        rpcAddress.getRpcAddress(),
                        stringBuilder.toString(),
                        rpcResult.getMsg());
                jobInstanceDO.setTriggerResult(0);
            }
        }
        jobInstanceDO.setTriggerWorker(sb.toString());
    }

}
