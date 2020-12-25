package com.sumu.common.util.rpc.feign;

import com.sumu.common.core.URLConstants;
import com.sumu.common.util.rpc.RpcResult;
import feign.Param;
import feign.RequestLine;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 21:21
 */
public interface FeignRequest {

    @RequestLine("PUT " + URLConstants.JOB_NOTIFY + "?" +
            URLConstants.HANDLER_NAME + "={handlerName}&" +
            URLConstants.JOB_INSTANCE_ID + "={jobInstanceID}&" +
            URLConstants.SHARD_INDEX + "={shardIndex}&" +
            URLConstants.SHARD_TOTAL + "={shardTotal}")
    RpcResult<Void> jobNotify(
            @Param("handlerName") String handlerName,
            @Param("jobInstanceID") String jobInstanceID,
            @Param("shardIndex") String shardIndex,
            @Param("shardTotal") String shardTotal
    );


}
