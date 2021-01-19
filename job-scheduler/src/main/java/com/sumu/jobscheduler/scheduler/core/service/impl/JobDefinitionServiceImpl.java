package com.sumu.jobscheduler.scheduler.core.service.impl;

import com.sumu.jobscheduler.scheduler.core.service.JobDefinitionService;
import com.sumu.jobscheduler.scheduler.interceptor.command.Command;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.definition.*;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.definition.java.CreateJavaJobDefinitionCommand;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.definition.java.JavaJobBuilder;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.definition.java.JavaJobDefinitionQueryCommand;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.definition.java.JavaJobQuery;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.definition.shell.CreateShellJobDefinitionCommand;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.definition.shell.ShellJobBuilder;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.definition.shell.ShellJobDefinitionQueryCommand;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.definition.shell.ShellJobQuery;
import com.sumu.jobscheduler.scheduler.interceptor.command.context.CommandContext;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.JobDefinition;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.java.JavaJobDefinition;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.shell.ShellJobDefinition;

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

    @Override
    public ShellJobBuilder createShellBuilder() {
        return this.commandExecutor.execute(new Command<ShellJobBuilder>() {
            @Override
            public ShellJobBuilder execute(CommandContext commandContext) {
                return new ShellJobBuilder(JobDefinitionServiceImpl.this);
            }
        });
    }

    @Override
    public ShellJobQuery createShellQuery() {
        return this.commandExecutor.execute(new Command<ShellJobQuery>() {
            @Override
            public ShellJobQuery execute(CommandContext commandContext) {
                return new ShellJobQuery(JobDefinitionServiceImpl.this);
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

    public Boolean createShellJob(ShellJobBuilder jobBuilder) {
        return this.commandExecutor.execute(new CreateShellJobDefinitionCommand(jobBuilder));
    }

    public ShellJobDefinition selectShellJob(ShellJobQuery shellJobQuery) {
        return this.commandExecutor.execute(new ShellJobDefinitionQueryCommand(shellJobQuery));
    }
}
