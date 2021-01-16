package com.sumu.jobscheduler.scheduler.config.auto;

import com.sumu.jobscheduler.scheduler.core.service.JobApplicationService;
import com.sumu.jobscheduler.scheduler.core.service.JobDefinitionService;
import com.sumu.jobscheduler.scheduler.core.service.JobInstanceService;
import com.sumu.jobscheduler.scheduler.core.service.WorkerService;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-08 09:13
 */
public interface JobEngine {

    JobApplicationService getJobApplicationService();

    WorkerService getWorkerService();

    JobDefinitionService getJobDefinitionService();

    JobInstanceService getJobInstanceService();
}
