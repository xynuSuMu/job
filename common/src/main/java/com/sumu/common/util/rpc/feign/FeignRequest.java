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

    @RequestLine("PUT " + URLConstants.JOB_NOTIFY + "?handlerName={handlerName}")
    RpcResult<Void> fire(@Param("handlerName") String handlerName);


}
