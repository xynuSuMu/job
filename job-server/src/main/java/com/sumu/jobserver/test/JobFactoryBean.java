package com.sumu.jobserver.test;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;

/**
 * @Auther: chenlong
 * @Date: 2021/1/7 20:50
 * @Description:
 */
public class JobFactoryBean implements FactoryBean<JobEngine>, ApplicationContextAware {

    protected ApplicationContext applicationContext;

    protected JobEngine jobEngine;

    protected SpringJobEngineConfiguration springJobEngineConfiguration;

    public JobFactoryBean(SpringJobEngineConfiguration springJobEngineConfiguration) {
        this.springJobEngineConfiguration = springJobEngineConfiguration;
    }

    @Nullable
    @Override
    public JobEngine getObject() throws Exception {
        this.jobEngine = this.springJobEngineConfiguration.buildProcessEngine();
        return jobEngine;
    }

    @Nullable
    @Override
    public Class<?> getObjectType() {
        return JobEngine.class;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
