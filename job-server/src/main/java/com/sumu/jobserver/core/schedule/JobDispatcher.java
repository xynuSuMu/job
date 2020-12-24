package com.sumu.jobserver.core.schedule;

import com.sumu.jobserver.enume.JobInfo;
import com.sumu.jobserver.mapper.JobMapper;
import com.sumu.jobserver.modal.job.JobDefinitionDO;
import com.sumu.jobserver.util.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-24 18:23
 */
@Component
public class JobDispatcher {

    @Autowired
    private JobMapper jobMapper;

    public void schedule(String jobDefinitionId) {
        JobDefinitionDO jobDefinitionDO = jobMapper.getJobDefinitionByID(jobDefinitionId);
        Class<? extends AbstractJobExecutor> clazz = JobInfo.Type.getClazzByCode(jobDefinitionDO.getTaskType());
        AbstractJobExecutor abstractJobExecutor = SpringContextUtils.getBean(clazz);
        abstractJobExecutor.executor(jobDefinitionId);
    }


}


