package com.sumu.jobserver.scheduler.config.auto;

import com.sumu.jobserver.scheduler.core.service.JobApplicationService;
import com.sumu.jobserver.scheduler.core.service.JobDefinitionService;
import com.sumu.jobserver.scheduler.core.service.JobInstanceService;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-08 09:53
 */
public class JobEngineImpl implements JobEngine {
    private JobApplicationService jobApplicationService;

    private JobDefinitionService jobDefinitionService;

    private JobInstanceService jobInstanceService;

    public JobEngineImpl(SpringJobConfiguration springJobConfiguration) {
        jobApplicationService = springJobConfiguration.getJobApplicationService();
        jobDefinitionService = springJobConfiguration.getJobDefinitionService();
        jobInstanceService = springJobConfiguration.getJobInstanceService();
    }

    @Override
    public JobApplicationService getJobApplicationService() {
        return jobApplicationService;
    }

    @Override
    public JobDefinitionService getJobDefinitionService() {
        return jobDefinitionService;
    }

    @Override
    public JobInstanceService getJobInstanceService() {
        return jobInstanceService;
    }
}
