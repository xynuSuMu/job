package com.sumu.jobserver.scheduler.interceptor.command.entity;

import com.sumu.jobserver.scheduler.config.auto.SpringJobConfiguration;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-10 10:52
 */
public class AbstractEntityManager {

    protected SpringJobConfiguration springJobConfiguration;

    public AbstractEntityManager(SpringJobConfiguration springJobConfiguration) {
        this.springJobConfiguration = springJobConfiguration;
    }

}
