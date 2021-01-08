package com.sumu.jobserver.scheduler.modal.job;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-24 11:14
 */
public class JavaJobDO {

    private int id;

    private int definitionID;



    private String handlerName;

    //策略，1-默认 2-集群 3-分片
    private int strategy;

    private int shardNum;



    public int getDefinitionID() {
        return definitionID;
    }

    public void setDefinitionID(int definitionID) {
        this.definitionID = definitionID;
    }

    public int getStrategy() {
        return strategy;
    }

    public void setStrategy(int strategy) {
        this.strategy = strategy;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShardNum() {
        return shardNum;
    }

    public void setShardNum(int shardNum) {
        this.shardNum = shardNum;
    }
}
