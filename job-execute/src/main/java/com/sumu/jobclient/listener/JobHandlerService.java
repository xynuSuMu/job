package com.sumu.jobclient.listener;

import com.sumu.jobclient.zk.ThreadRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-04 15:11
 */
public class JobHandlerService implements ApplicationListener<ContextRefreshedEvent> {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    private ThreadRegister threadRegister;

    //IOC容器启动后
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        threadRegister = new ThreadRegister();
        LOG.info("[Job Client] Register ThreadPool");
        threadRegister.register();
        LOG.info("[Job Client] Register ThreadPool finish ");
    }

}
