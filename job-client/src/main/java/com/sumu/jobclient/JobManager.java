package com.sumu.jobclient;

import com.sumu.jobclient.annotation.JobHandler;
import com.sumu.jobclient.common.Context;
import com.sumu.jobclient.handler.AbstractJobHandler;
import com.sumu.jobclient.modal.job.JobData;
import com.sumu.jobclient.properties.AppProperties;
import com.sumu.jobclient.properties.JobProperties;
import com.sumu.jobclient.rpc.JettyServer;
import com.sumu.jobclient.zk.JobRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;


/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-04 10:05
 */
public class JobManager implements ApplicationContextAware {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    private JettyServer jettyServer;


    private JobRegister jobRegister;


    public JobManager(JobProperties jobProperties, AppProperties appProperties) {
        Context.setJobProperties(jobProperties);
        Context.setAppProperties(appProperties);
    }


    public void start() throws Exception {
        //init jobList
        Map<String, AbstractJobHandler> jobHandlerMap = initJobHandlers();
        //Job Data
        JobData jobData = new JobData(jobHandlerMap);

        jettyServer = new JettyServer(jobData);

        jobRegister = new JobRegister();

        //JOB REGISTER
        jobRegister.register();

        //jetty start
        jettyServer.start();
    }

    public void destroy() {
        LOG.info("[Job] Manager destroy");
        jettyServer.destroy();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Context.setApplicationContext(applicationContext);
    }

    private Map<String, AbstractJobHandler> initJobHandlers() {
        Map<String, AbstractJobHandler> jobHandlerMap = new HashMap<>();
        ApplicationContext applicationContext = Context.getApplicationContext();

        Assert.notNull(applicationContext, "applicationContext is null!");

        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(JobHandler.class);

        for (Object serviceBean : serviceBeanMap.values()) {
            if (serviceBean instanceof AbstractJobHandler) {
                String name = serviceBean.getClass().getAnnotation(JobHandler.class).value();
                AbstractJobHandler handler = (AbstractJobHandler) serviceBean;
                if (jobHandlerMap.containsKey(name)) {
                    throw new RuntimeException("zk naming repeat : " + name);
                }
                jobHandlerMap.put(name, handler);
            }
        }
        return jobHandlerMap;
    }
}
