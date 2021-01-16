package com.sumu.jobscheduler.scheduler.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 19:43
 */
@ConfigurationProperties("job")
public class JobProperties {

    private String zkAddress;

    private String specialApp;

    public String getSpecialApp() {
        return specialApp;
    }

    public void setSpecialApp(String specialApp) {
        this.specialApp = specialApp;
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

}
