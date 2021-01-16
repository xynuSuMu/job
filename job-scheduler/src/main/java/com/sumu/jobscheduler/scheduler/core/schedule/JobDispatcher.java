package com.sumu.jobscheduler.scheduler.core.schedule;

import com.sumu.jobscheduler.scheduler.core.service.JobDefinitionService;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.JobDefinition;
import com.sumu.jobscheduler.scheduler.modal.enume.JobInfo;
import com.sumu.jobscheduler.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-24 18:23
 */
public class JobDispatcher {

    private Logger LOG = LoggerFactory.getLogger(JobDispatcher.class);

    private final ThreadPoolExecutor triggerPool = new ThreadPoolExecutor(
            50,
            500,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100000),
            new ThreadPoolExecutor.CallerRunsPolicy());


//    @Autowired
    private JobDefinitionService jobDefinitionService;

    public JobDispatcher(JobDefinitionService jobDefinitionService) {
        this.jobDefinitionService = jobDefinitionService;
    }

    public void schedule(String jobDefinitionId) {
        LOG.info("[JobDefinitionId={}] Executor Job ", jobDefinitionId);
        JobDefinition jobDefinitionDO =
                jobDefinitionService.createQuery()
                        .id(Integer.valueOf(jobDefinitionId))
                        .singleResult();
        Class<? extends AbstractJobExecutor> clazz = JobInfo.Type.getClazzByCode(jobDefinitionDO.getTaskType());
        AbstractJobExecutor abstractJobExecutor = SpringContextUtils.getBean(clazz);
        triggerPool.submit(() -> {
            abstractJobExecutor.executor(jobDefinitionId);
        });
    }


}


