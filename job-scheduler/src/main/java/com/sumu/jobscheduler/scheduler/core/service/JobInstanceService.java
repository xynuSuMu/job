package com.sumu.jobscheduler.scheduler.core.service;

import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.instance.JobInstanceBuilder;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.instance.JobInstanceQuery;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-08 09:25
 */
public interface JobInstanceService {

    JobInstanceBuilder createBuilder();

    JobInstanceQuery createQuery();

}
