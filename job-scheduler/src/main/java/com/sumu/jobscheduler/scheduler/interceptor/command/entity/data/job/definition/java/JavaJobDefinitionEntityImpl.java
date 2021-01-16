package com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.java;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-12 14:09
 */
public class JavaJobDefinitionEntityImpl implements JavaJobDefinitionEntity {

    private int id;
    private int definitionID;
    private String handlerName;
    private int strategy;
    private int shardNum;

    public int getId() {
        return id;
    }

    public int getDefinitionID() {
        return definitionID;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public int getStrategy() {
        return strategy;
    }

    public int getShardNum() {
        return shardNum;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDefinitionID(int definitionID) {
        this.definitionID = definitionID;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public void setStrategy(int strategy) {
        this.strategy = strategy;
    }

    public void setShardNum(int shardNum) {
        this.shardNum = shardNum;
    }
}
