package com.sumu.jobscheduler.scheduler.config.auto;

import com.sumu.jobscheduler.scheduler.core.service.JobApplicationService;
import com.sumu.jobscheduler.scheduler.core.service.JobDefinitionService;
import com.sumu.jobscheduler.scheduler.core.service.JobInstanceService;
import com.sumu.jobscheduler.scheduler.core.service.WorkerService;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-08 09:53
 */
public class JobEngineImpl implements JobEngine {
    private JobApplicationService jobApplicationService;

    private JobDefinitionService jobDefinitionService;

    private JobInstanceService jobInstanceService;

    private WorkerService workerService;

    public JobEngineImpl(SpringJobConfiguration springJobConfiguration) {
        jobApplicationService = springJobConfiguration.getJobApplicationService();
        jobDefinitionService = springJobConfiguration.getJobDefinitionService();
        jobInstanceService = springJobConfiguration.getJobInstanceService();
        workerService = springJobConfiguration.getWorkerService();
    }

    @Override
    public JobApplicationService getJobApplicationService() {
        return jobApplicationService;
    }

    @Override
    public WorkerService getWorkerService() {
        return workerService;
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
