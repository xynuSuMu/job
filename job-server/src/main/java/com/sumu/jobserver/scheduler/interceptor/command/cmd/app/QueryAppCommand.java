package com.sumu.jobserver.scheduler.interceptor.command.cmd.app;

import com.sumu.jobserver.scheduler.interceptor.command.Command;
import com.sumu.jobserver.scheduler.interceptor.command.context.CommandContext;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.app.App;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.app.AppEntityImpl;
import com.sumu.jobserver.scheduler.mapper.AppMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-11 14:00
 */
public class QueryAppCommand implements Command<List<App>> {

    private AppQuery appQuery;

    public QueryAppCommand(AppQuery appQuery) {
        this.appQuery = appQuery;
    }

    @Override
    public List<App> execute(CommandContext commandContext) {
        AppMapper appMapper = commandContext.getSqlSession().getMapper(AppMapper.class);
        List<AppEntityImpl> list = appMapper.queryApp(appQuery.getAppEntity());
        List<App> res = new ArrayList<>();
        list.stream().forEach(appEntity -> {
            res.add(appEntity);
        });
        return res;
    }
}
