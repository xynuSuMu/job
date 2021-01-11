package com.sumu.jobserver.scheduler.core.service;

import com.sumu.jobserver.scheduler.interceptor.command.cmd.worker.WorkerBuilder;
import com.sumu.jobserver.scheduler.interceptor.command.cmd.worker.WorkerQuery;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-11 10:53
 */
public interface WorkerService {

    WorkerBuilder createBuilder();

    WorkerQuery createQuery();

}
