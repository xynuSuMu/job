package com.sumu.jobserver.enume;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-25 09:16
 */
public interface JavaJobInfo {

    enum Strategy {
        DEFAULT(1, "默认策略-集群"),
        BROADCAST(2, "广播策略"),
        SHARD(3, "分片"),
        ;
        private int code;
        private String decs;

        Strategy(int code, String decs) {
            this.code = code;
            this.decs = decs;
        }

        public int getCode() {
            return code;
        }

        public String getDecs() {
            return decs;
        }

        public static Strategy getStrategy(int code) {
            for (Strategy strategy : values()) {
                if (code == strategy.getCode()) {
                    return strategy;
                }
            }
            return null;
        }
    }
}
