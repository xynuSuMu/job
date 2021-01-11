package com.sumu.jobserver.scheduler.interceptor.command.cmd.app;

import com.sumu.jobserver.scheduler.interceptor.command.Command;
import com.sumu.jobserver.scheduler.interceptor.command.context.CommandContext;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.app.App;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.app.AppEntity;
import com.sumu.jobserver.scheduler.mapper.AppMapper;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-11 11:14
 */
public class CreateAppCommand implements Command<App> {

    private AppBuilder appBuilder;

    public CreateAppCommand(AppBuilder appBuilder) {
        this.appBuilder = appBuilder;
    }

    @Override
    public App execute(CommandContext commandContext) {
        AppEntity appEntity = appBuilder.getAppEntity();
        AppMapper appMapper = commandContext.getSqlSession().getMapper(AppMapper.class);
        appMapper.insertAppCode(appEntity.getAppCode(), appEntity.getZxID());
        App app = appMapper.getByAppCode(appEntity.getAppCode());
        return app;
    }

}
