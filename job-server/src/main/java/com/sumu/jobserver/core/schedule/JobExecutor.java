package com.sumu.jobserver.core.schedule;

import com.sumu.jobserver.mapper.JobMapper;
import com.sumu.jobserver.modal.job.JobInstanceDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

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

    public void executorByQuartz(String jobDefinitionId, Date fireTime, Date scheduledFireTime) {
        //Job实例
        JobInstanceDO jobInstanceDO = new JobInstanceDO();
        jobInstanceDO.setJobDefinitionId(Integer.valueOf(jobDefinitionId));
        jobInstanceDO.setStartTime(new Date());
        jobInstanceDO.setTriggerType(1);//1-自动 0-手动
        //todo:调度
        jobInstanceDO.setTriggerWorker("");//调度的机器地址
        jobInstanceDO.setTriggerResult(1);
        //
        jobInstanceDO.setEndTime(new Date());
        jobMapper.insertJobInstance(jobInstanceDO);
        LOG.info("执行Job,{},{},{}", jobDefinitionId, fireTime, scheduledFireTime);
    }
}
