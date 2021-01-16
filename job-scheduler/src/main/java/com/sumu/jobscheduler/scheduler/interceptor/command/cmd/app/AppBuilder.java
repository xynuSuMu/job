package com.sumu.jobscheduler.scheduler.interceptor.command.cmd.app;

import com.sumu.jobscheduler.scheduler.core.service.impl.JobApplicationServiceImpl;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.app.App;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.app.AppEntity;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.app.AppEntityImpl;

import java.util.Date;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-11 10:59
 */
public class AppBuilder {

    private AppEntity appEntity;

    private JobApplicationServiceImpl jobApplicationService;

    public AppBuilder(JobApplicationServiceImpl jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
        this.appEntity = new AppEntityImpl();
    }

    public AppBuilder id(int id) {
        appEntity.setID(id);
        return this;
    }

    public AppBuilder appCode(String appCode) {
        appEntity.setAppCode(appCode);
        return this;
    }

    public AppBuilder zxID(long zxID) {
        appEntity.setZxID(zxID);
        return this;
    }

    public AppBuilder updateTime(Date updateTime) {
        appEntity.setUpdateTime(updateTime);
        return this;
    }

    public AppEntity getAppEntity() {
        return appEntity;
    }

    public App create() {
        App app = this.jobApplicationService.create(this);
        return app;
    }
}
