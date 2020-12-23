package com.sumu.jobserver.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-23 15:16
 * @desc SpringApplicationRunListeners 、 SpringApplicationRunListener 、 ApplicationListener的区别 -> SpringApplication源码
 */
public class JobServerListener implements SpringApplicationRunListener {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    private SpringApplication springApplication;
    private String[] args;


    public JobServerListener(SpringApplication springApplication, String[] args) {
        this.springApplication = springApplication;
        this.args = args;
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        //启动完成
        LOG.info("[Job Server] start started");
    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        //
        LOG.info("[Job Server] start runing");
        //发送钉钉通知
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        //启动失败
        LOG.info("[Job Server] start fail");
        //发送短信/钉钉/异常
    }
}
