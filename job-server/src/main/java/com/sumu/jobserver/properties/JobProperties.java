package com.sumu.jobserver.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 19:43
 */
@Component
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
