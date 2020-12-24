package com.sumu.jobserver.core.schedule;

import com.sumu.jobserver.mapper.JobMapper;
import com.sumu.jobserver.modal.job.JobDefinitionDO;
import com.sumu.jobserver.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-24 18:14
 */
public abstract class AbstractJobExecutor {

    private Logger LOG = LoggerFactory.getLogger(AbstractJobExecutor.class);

    private JobDefinitionDO prepareJobDefinition(String jobDefinitionId) {
        JobMapper jobMapper = SpringContextUtils.getBean(JobMapper.class);
        JobDefinitionDO jobDefinitionDO = jobMapper.getJobDefinitionByID(jobDefinitionId);
        return jobDefinitionDO;
    }

    public void executor(String jobDefinitionId) {
        JobDefinitionDO jobDefinitionDO = this.prepareJobDefinition(jobDefinitionId);
        executorByQuartz(jobDefinitionDO);
        finish(jobDefinitionDO);
    }

    public abstract void executorByQuartz(JobDefinitionDO jobDefinitionDO);

    public void finish(JobDefinitionDO jobDefinitionDO) {
        LOG.info("[ Job Schedule Finish ] jobName={},cron={}", jobDefinitionDO.getJobName(), jobDefinitionDO.getCron());
    }

}
