package com.sumu.jobclient;

import com.sumu.jobclient.common.Context;
import com.sumu.jobclient.properties.AppProperties;
import com.sumu.jobclient.properties.JobProperties;
import com.sumu.jobclient.rpc.JettyServer;
import com.sumu.jobclient.zk.JobRegister;
import com.sumu.jobclient.zk.ThreadRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-04 10:05
 */
public class JobManager implements ApplicationContextAware {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    private JettyServer jettyServer;

    private ThreadRegister threadRegister;

    private JobRegister jobRegister;


    public JobManager(JobProperties jobProperties, AppProperties appProperties) {
        jettyServer = new JettyServer();
        threadRegister = new ThreadRegister();
        jobRegister = new JobRegister();
        Context.setJobProperties(jobProperties);
        Context.setAppProperties(appProperties);
    }


    public void start() throws Exception {
        //JOB REGISTER
        jobRegister.register();
        //Thread REGISTER
        threadRegister.register();
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
//        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(JobHandler.class);
    }
}
