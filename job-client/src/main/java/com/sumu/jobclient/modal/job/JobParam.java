package com.sumu.jobclient.modal.job;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-25 10:27
 */
public class JobParam {

    private String jobInstanceID;

    private String shardIndex;

    private String shardTotal;

    public JobParam(String jobInstanceID, String shardIndex, String shardTotal) {
        this.jobInstanceID = jobInstanceID;
        this.shardIndex = shardIndex;
        this.shardTotal = shardTotal;
    }

    public String getJobInstanceID() {
        return jobInstanceID;
    }

    public String getShardIndex() {
        return shardIndex;
    }

    public String getShardTotal() {
        return shardTotal;
    }
}
