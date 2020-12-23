package com.sumu.jobclient.listener;

import com.sumu.jobclient.zk.ThreadRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-23 16:56
 * @desc
 */
public class JobClientListener implements SpringApplicationRunListener {


    private Logger LOG = LoggerFactory.getLogger(this.getClass());


    private SpringApplication springApplication;

    private String[] args;


    private ThreadRegister threadRegister;


    public JobClientListener(SpringApplication springApplication, String[] args) {
        this.args = args;
        this.springApplication = springApplication;
        threadRegister = new ThreadRegister();
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        LOG.info("[Job Client] Register ThreadPool");
        threadRegister.register();
        LOG.info("[Job Client] Register ThreadPool finish ");
    }
}
