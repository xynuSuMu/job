package com.sumu.jobserver.scheduler.interceptor.command.cmd.app;

import com.sumu.jobserver.scheduler.core.service.impl.JobApplicationServiceImpl;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.app.App;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.app.AppEntity;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.app.AppEntityImpl;


import java.util.Date;
import java.util.List;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-11 13:44
 */
public class AppQuery {

    private AppEntity appEntity;

    private JobApplicationServiceImpl jobApplicationService;

    public AppQuery(JobApplicationServiceImpl jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
        this.appEntity = new AppEntityImpl();
    }

    public AppEntity getAppEntity() {
        return appEntity;
    }

    public AppQuery id(int id) {
        appEntity.setID(id);
        return this;
    }

    public AppQuery appCode(String appCode) {
        appEntity.setAppCode(appCode);
        return this;
    }

    public AppQuery zxID(long zxID) {
        appEntity.setZxID(zxID);
        return this;
    }

    public AppQuery updateTime(Date updateTime) {
        appEntity.setUpdateTime(updateTime);
        return this;
    }

    public List<App> list() {
        return jobApplicationService.query(this);
    }

    public App singleResult() {
        List<App> list = jobApplicationService.query(this);
        if (list == null || list.size() == 0) {
            return null;
        }
        App app = list.get(0);
        return app;
    }

}
