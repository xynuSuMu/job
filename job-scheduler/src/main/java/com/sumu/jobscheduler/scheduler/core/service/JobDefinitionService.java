package com.sumu.jobscheduler.scheduler.core.service;

import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.definition.JobDefinitionBuilder;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.definition.JobDefinitionQuery;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.definition.java.JavaJobBuilder;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.definition.java.JavaJobQuery;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.definition.shell.ShellJobBuilder;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.definition.shell.ShellJobQuery;

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

    ShellJobBuilder createShellBuilder();

    ShellJobQuery createShellQuery();

}
