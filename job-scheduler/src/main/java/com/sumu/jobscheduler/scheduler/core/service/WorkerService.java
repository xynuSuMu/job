package com.sumu.jobscheduler.scheduler.core.service;

import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.worker.WorkerBuilder;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.worker.WorkerQuery;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-11 10:53
 */
public interface WorkerService {

    WorkerBuilder createBuilder();

    WorkerQuery createQuery();

}
