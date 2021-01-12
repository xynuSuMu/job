package com.sumu.jobserver.scheduler.core.service.impl;

import com.sumu.jobserver.scheduler.core.service.JobInstanceService;
import com.sumu.jobserver.scheduler.interceptor.command.Command;
import com.sumu.jobserver.scheduler.interceptor.command.cmd.job.instance.CreateJobInstanceCommand;
import com.sumu.jobserver.scheduler.interceptor.command.cmd.job.instance.JobInstanceBuilder;
import com.sumu.jobserver.scheduler.interceptor.command.cmd.job.instance.JobInstanceQuery;
import com.sumu.jobserver.scheduler.interceptor.command.cmd.job.instance.JobInstanceQueryCommand;
import com.sumu.jobserver.scheduler.interceptor.command.context.CommandContext;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.instance.JobInstance;

import java.util.List;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-08 09:25
 */
public class JobInstanceServiceImpl extends ServiceImpl implements JobInstanceService {

    @Override
    public JobInstanceBuilder createBuilder() {
        return this.commandExecutor.execute(new Command<JobInstanceBuilder>() {
            @Override
            public JobInstanceBuilder execute(CommandContext commandContext) {
                return new JobInstanceBuilder(JobInstanceServiceImpl.this);
            }
        });

    }

    @Override
    public JobInstanceQuery createQuery() {
        return this.commandExecutor.execute(new Command<JobInstanceQuery>() {
            @Override
            public JobInstanceQuery execute(CommandContext commandContext) {
                return new JobInstanceQuery(JobInstanceServiceImpl.this);
            }
        });
    }

    public List<JobInstance> list(JobInstanceQuery jobInstanceQuery) {
        return this.commandExecutor.execute(new JobInstanceQueryCommand(jobInstanceQuery));
    }

    public JobInstance create(JobInstanceBuilder jobInstanceBuilder) {
        return this.commandExecutor.execute(new CreateJobInstanceCommand(jobInstanceBuilder));
    }
}
