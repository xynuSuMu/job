package com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.java;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-12 14:08
 */
public interface JavaJobDefinition {

    int getId();

    int getDefinitionID();

    String getHandlerName();

    int getStrategy();

    int getShardNum();

}
