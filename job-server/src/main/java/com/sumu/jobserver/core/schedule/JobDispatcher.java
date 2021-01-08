package com.sumu.jobserver.core.schedule;

import com.sumu.jobserver.modal.enume.JobInfo;
import com.sumu.jobserver.mapper.JobMapper;
import com.sumu.jobserver.modal.job.JobDefinitionDO;
import com.sumu.jobserver.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-24 18:23
 */
@Component
public class JobDispatcher {

    private Logger LOG = LoggerFactory.getLogger(JobDispatcher.class);

    private final ThreadPoolExecutor triggerPool = new ThreadPoolExecutor(
            50,
            500,
            60L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(100000),
            new ThreadPoolExecutor.CallerRunsPolicy());

    @Autowired
    private JobMapper jobMapper;

    public void schedule(String jobDefinitionId) {
        LOG.info("[JobDefinitionId={}] Executor Job ", jobDefinitionId);
        JobDefinitionDO jobDefinitionDO = jobMapper.getJobDefinitionByID(jobDefinitionId);
        Class<? extends AbstractJobExecutor> clazz = JobInfo.Type.getClazzByCode(jobDefinitionDO.getTaskType());
        AbstractJobExecutor abstractJobExecutor = SpringContextUtils.getBean(clazz);
        triggerPool.submit(() -> {
            abstractJobExecutor.executor(jobDefinitionId);
        });
    }


}


