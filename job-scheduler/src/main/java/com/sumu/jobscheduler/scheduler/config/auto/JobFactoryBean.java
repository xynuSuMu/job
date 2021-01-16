package com.sumu.jobscheduler.scheduler.config.auto;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-08 09:11
 */
public class JobFactoryBean implements FactoryBean<JobEngine>, ApplicationContextAware {

    protected ApplicationContext applicationContext;

    protected SpringJobConfiguration springJobConfiguration;

    protected JobEngine jobEngine;

    public JobFactoryBean(SpringJobConfiguration springJobConfiguration) {
        this.springJobConfiguration = springJobConfiguration;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public JobEngine getObject() throws Exception {
        this.jobEngine = this.springJobConfiguration.jobEngine();
        return jobEngine;
    }

    @Override
    public Class<?> getObjectType() {
        return JobEngine.class;
    }
}
