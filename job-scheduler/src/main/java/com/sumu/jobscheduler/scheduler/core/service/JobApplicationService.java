package com.sumu.jobscheduler.scheduler.core.service;

import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.app.AppBuilder;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.app.AppQuery;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.worker.WorkerBuilder;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-08 09:26
 */
public interface JobApplicationService {

    AppBuilder createAppBuilder();

    AppQuery createAppQuery();
}
