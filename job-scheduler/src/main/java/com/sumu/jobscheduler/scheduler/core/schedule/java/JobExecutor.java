package com.sumu.jobscheduler.scheduler.core.schedule.java;

import com.sumu.common.util.rpc.RpcAddress;
import com.sumu.common.util.rpc.RpcResult;
import com.sumu.common.util.rpc.feign.FeignUtil;
import com.sumu.jobscheduler.scheduler.core.schedule.AbstractJobExecutor;
import com.sumu.jobscheduler.scheduler.core.schedule.JobDispatcher;
import com.sumu.jobscheduler.scheduler.core.service.JobDefinitionService;
import com.sumu.jobscheduler.scheduler.core.service.JobInstanceService;
import com.sumu.jobscheduler.scheduler.core.service.WorkerService;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.JobDefinition;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.java.JavaJobDefinition;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.instance.JobInstance;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.worker.Worker;
import com.sumu.jobscheduler.scheduler.modal.enume.JavaJobInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 15:41
 */
public class JobExecutor extends AbstractJobExecutor {

    private Logger LOG = LoggerFactory.getLogger(JobExecutor.class);


    private WorkerService workerService;


    private JobDefinitionService jobDefinitionService;


    private JobInstanceService jobInstanceService;


    private JobDispatcher jobDispatcher;

    public JobExecutor(WorkerService workerService,
                       JobDefinitionService jobDefinitionService,
                       JobInstanceService jobInstanceService,
                       JobDispatcher jobDispatcher) {
        this.workerService = workerService;
        this.jobDefinitionService = jobDefinitionService;
        this.jobInstanceService = jobInstanceService;
        this.jobDispatcher = jobDispatcher;
    }

    @Override
    public void executorByQuartz(JobDefinition jobDefinitionDO) {
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
        JavaJobDefinition javaJobDO = jobDefinitionService.createJavaQuery()
                .definitionId(jobDefinitionId)
                .singleResult();
        String handlerName = javaJobDO.getHandlerName();
        int strategy = javaJobDO.getStrategy();
        //获取AppID注册的机器
        List<Worker> workers = workerService.createQuery()
                .appId(appId)
                .enable(true)
                .list();

        //Job实例
        JobInstance jobInstanceDO = jobInstanceService.createBuilder()
                .jobDefinitionId(jobDefinitionId)
                .startTime(new Date())
                .triggerType(1)
                .create();
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

    }

    private void updateInstance(int instanceId, int result, String worker) {
        Date endTime = new Date();
        jobInstanceService.createBuilder()
                .id(instanceId)
                .endTime(endTime)
                .triggerResult(result)
                .triggerWorker(worker)
                .create();
    }

    private void defaultStrategy(List<Worker> workers, String handlerName, JobInstance jobInstanceDO) {
        StringBuilder sb = new StringBuilder();
        int result = 0;
        for (Worker workerDO : workers) {
            RpcAddress rpcAddress = new RpcAddress(workerDO.getIp(), workerDO.getPort());
            sb.append(rpcAddress.getRpcAddress() + " ");
            LOG.info("RpcAddress,{},handlerName,{}", rpcAddress.getRpcAddress(), handlerName);
            RpcResult<Void> rpcResult = FeignUtil.jobNotify(rpcAddress,
                    handlerName,
                    String.valueOf(jobInstanceDO.getId()));
            if (!rpcResult.isSuccess()) {
                LOG.error("Error , {}", rpcResult.getMsg());
                continue;
            }
            result = rpcResult.isSuccess() ? 1 : 0;
            break;
        }
        updateInstance(jobInstanceDO.getId(), result, sb.toString());
    }

    private void clusterStrategy(List<Worker> workers, String handlerName, JobInstance jobInstanceDO) {
        StringBuilder sb = new StringBuilder();
        int result = 0;
        for (Worker workerDO : workers) {
            RpcAddress rpcAddress = new RpcAddress(workerDO.getIp(), workerDO.getPort());
            sb.append(rpcAddress.getRpcAddress() + " ");
            LOG.info("RpcAddress,{},handlerName,{}", rpcAddress.getRpcAddress(), handlerName);
            RpcResult<Void> rpcResult = FeignUtil.jobNotify(rpcAddress,
                    handlerName,
                    String.valueOf(jobInstanceDO.getId()));
            if (!rpcResult.isSuccess()) {
                LOG.error("Error , {}", rpcResult.getMsg());
            }
            result = rpcResult.isSuccess() ? 1 : 0;
        }

        updateInstance(jobInstanceDO.getId(), result, sb.toString());
    }

    //分片策略
    private void shardStrategy(int toatal, List<Worker> workers, String handlerName, JobInstance jobInstanceDO) {
        //
        Map<Worker, List<Integer>> map = new HashMap<>();
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
        Set<Map.Entry<Worker, List<Integer>>> set = map.entrySet();
        StringBuilder sb = new StringBuilder();
        int result = 0;
        for (Map.Entry<Worker, List<Integer>> entry : set) {
            Worker workerDO = entry.getKey();
            //分片索引
            List<Integer> shardIndex = entry.getValue();
            StringBuilder stringBuilder = new StringBuilder();
            for (Integer i : shardIndex) {
                stringBuilder.append(i + ",");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            RpcAddress rpcAddress = new RpcAddress(workerDO.getIp(), workerDO.getPort());
            sb.append(rpcAddress.getRpcAddress() + " ");
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
                result = 0;
            }
        }

        updateInstance(jobInstanceDO.getId(), result, sb.toString());
    }

}