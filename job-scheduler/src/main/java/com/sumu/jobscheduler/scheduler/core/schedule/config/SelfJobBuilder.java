package com.sumu.jobscheduler.scheduler.core.schedule.config;

import org.quartz.Job;
import org.quartz.JobBuilder;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-16 20:28
 */
public class SelfJobBuilder extends JobBuilder {

    private String appCode;

    public static SelfJobBuilder newSelfJob(Class<? extends Job> jobClass) {
        SelfJobBuilder b = new SelfJobBuilder();
        b.ofType(jobClass);
        return b;
    }

    @Override
    public SelfJobBuilder withIdentity(String name, String group) {
        super.withIdentity(name, group);
        return this;
    }

    public SelfJobBuilder appCode(String appCode) {
        this.appCode = appCode;
        return this;
    }

}
