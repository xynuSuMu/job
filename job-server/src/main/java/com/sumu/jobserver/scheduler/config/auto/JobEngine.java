package com.sumu.jobserver.scheduler.config.auto;

import com.sumu.jobserver.scheduler.core.service.JobApplicationService;
import com.sumu.jobserver.scheduler.core.service.JobDefinitionService;
import com.sumu.jobserver.scheduler.core.service.JobInstanceService;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-08 09:13
 */
public interface JobEngine {

    JobApplicationService getJobApplicationService();

    JobDefinitionService getJobDefinitionService();

    JobInstanceService getJobInstanceService();
}
