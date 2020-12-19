package com.sumu.jobclient.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-19 15:49
 */

@ConfigurationProperties("job")
public class JobProperties {
    private String zkAddress;

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }
}
