package com.sumu.jobclient.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-19 15:49
 */

@ConfigurationProperties("app")
public class AppProperties {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
