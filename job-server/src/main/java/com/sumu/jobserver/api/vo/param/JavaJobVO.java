package com.sumu.jobserver.api.vo.param;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-24 10:59
 */
public class JavaJobVO {

    private String handlerName;

    //策略，1-默认 2-集群 3-分片
    private int strategy;

    //分片数
    private int shardNum;

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

    public int getShardNum() {
        return shardNum;
    }

    public void setShardNum(int shardNum) {
        this.shardNum = shardNum;
    }
}
