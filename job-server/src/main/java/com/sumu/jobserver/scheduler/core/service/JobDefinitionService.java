package com.sumu.jobserver.scheduler.core.service;

import com.sumu.jobserver.scheduler.interceptor.command.cmd.job.definition.JobDefinitionBuilder;
import com.sumu.jobserver.scheduler.interceptor.command.cmd.job.definition.JobDefinitionQuery;
import com.sumu.jobserver.scheduler.interceptor.command.cmd.job.definition.java.JavaJobBuilder;
import com.sumu.jobserver.scheduler.interceptor.command.cmd.job.definition.java.JavaJobQuery;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-08 09:24
 */
public interface JobDefinitionService {

    JobDefinitionBuilder createBuilder();

    JobDefinitionQuery createQuery();

    JavaJobBuilder createJavaBuilder();

    JavaJobQuery createJavaQuery();

}
