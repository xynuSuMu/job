package com.sumu.jobscheduler.scheduler.core.service.impl;

import com.sumu.jobscheduler.scheduler.core.service.JobDefinitionService;
import com.sumu.jobscheduler.scheduler.interceptor.command.Command;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.definition.*;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.definition.java.CreateJavaJobDefinitionCommand;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.definition.java.JavaJobBuilder;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.definition.java.JavaJobDefinitionQueryCommand;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.definition.java.JavaJobQuery;
import com.sumu.jobscheduler.scheduler.interceptor.command.context.CommandContext;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.JobDefinition;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.java.JavaJobDefinition;

import java.util.List;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-08 09:25
 */
public class JobDefinitionServiceImpl extends ServiceImpl implements JobDefinitionService {

    @Override
    public JobDefinitionBuilder createBuilder() {
        return this.commandExecutor.execute(new Command<JobDefinitionBuilder>() {
            @Override
            public JobDefinitionBuilder execute(CommandContext commandContext) {
                return new JobDefinitionBuilder(JobDefinitionServiceImpl.this);
            }
        });
    }

    @Override
    public JobDefinitionQuery createQuery() {
        return this.commandExecutor.execute(new Command<JobDefinitionQuery>() {
            @Override
            public JobDefinitionQuery execute(CommandContext commandContext) {
                return new JobDefinitionQuery(JobDefinitionServiceImpl.this);
            }
        });
    }

    @Override
    public JavaJobBuilder createJavaBuilder() {
        return this.commandExecutor.execute(new Command<JavaJobBuilder>() {
            @Override
            public JavaJobBuilder execute(CommandContext commandContext) {
                return new JavaJobBuilder(JobDefinitionServiceImpl.this);
            }
        });
    }

    @Override
    public JavaJobQuery createJavaQuery() {
        return this.commandExecutor.execute(new Command<JavaJobQuery>() {
            @Override
            public JavaJobQuery execute(CommandContext commandContext) {
                return new JavaJobQuery(JobDefinitionServiceImpl.this);
            }
        });
    }

    public JobDefinition create(JobDefinitionBuilder jobDefinitionBuilder) {
        return this.commandExecutor.execute(new CreateJobDefinitionCommand(jobDefinitionBuilder));
    }

    public List<JobDefinition> list(JobDefinitionQuery jobDefinitionQuery) {
        return this.commandExecutor.execute(new JobDefinitionQueryCommand(jobDefinitionQuery));
    }

    public Integer count(JobDefinitionQuery jobDefinitionQuery) {
        return this.commandExecutor.execute(new JobDefinitionCountCommand(jobDefinitionQuery));
    }

    public JavaJobDefinition createJavaJob(JavaJobBuilder javaJobBuilder) {
        return this.commandExecutor.execute(new CreateJavaJobDefinitionCommand(javaJobBuilder));
    }

    public JavaJobDefinition selectJavaJob(JavaJobQuery javaJobQuery) {
        return this.commandExecutor.execute(new JavaJobDefinitionQueryCommand(javaJobQuery));
    }

    public Boolean deleteJobDefinition(JobDefinitionBuilder jobDefinitionBuilder) {
        return this.commandExecutor.execute(new DeleteJobDefinitionCommand(jobDefinitionBuilder));
    }
}
