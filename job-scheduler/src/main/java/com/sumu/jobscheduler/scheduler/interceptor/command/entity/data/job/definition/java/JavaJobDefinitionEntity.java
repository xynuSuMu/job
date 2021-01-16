package com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.java;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-12 14:09
 */
public interface JavaJobDefinitionEntity extends JavaJobDefinition {

    void setId(int id);

    void setDefinitionID(int definitionID);

    void setHandlerName(String handlerName);

    void setStrategy(int strategy);

    void setShardNum(int shardNum);
}
