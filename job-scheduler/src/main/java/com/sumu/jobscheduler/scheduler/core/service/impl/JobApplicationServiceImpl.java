package com.sumu.jobscheduler.scheduler.core.service.impl;

import com.sumu.jobscheduler.scheduler.core.service.JobApplicationService;
import com.sumu.jobscheduler.scheduler.interceptor.command.Command;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.app.AppBuilder;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.app.AppQuery;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.app.CreateAppCommand;
import com.sumu.jobscheduler.scheduler.interceptor.command.cmd.app.QueryAppCommand;
import com.sumu.jobscheduler.scheduler.interceptor.command.context.CommandContext;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.app.App;

import java.util.List;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-08 09:26
 */
public class JobApplicationServiceImpl extends ServiceImpl implements JobApplicationService {


    @Override
    public AppBuilder createAppBuilder() {
        return this.commandExecutor.execute(new Command<AppBuilder>() {
            @Override
            public AppBuilder execute(CommandContext commandContext) {
                return new AppBuilder(JobApplicationServiceImpl.this);
            }
        });
    }

    @Override
    public AppQuery createAppQuery() {
        return this.commandExecutor.execute(new Command<AppQuery>() {
            @Override
            public AppQuery execute(CommandContext commandContext) {
                return new AppQuery(JobApplicationServiceImpl.this);
            }
        });
    }

    public App create(AppBuilder appBuilder) {
        return this.commandExecutor.execute(new CreateAppCommand(appBuilder));
    }

    public List<App> query(AppQuery appQuery) {
        return this.commandExecutor.execute(new QueryAppCommand(appQuery));
    }

}
