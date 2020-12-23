package com.sumu.common.modal.rpc.feign;

import com.sumu.common.modal.rpc.RpcResult;
import feign.Param;
import feign.RequestLine;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 21:21
 */
public interface FeignRequest {

    @RequestLine("PUT /job/notify?handlerName={handlerName}")
    RpcResult<Void> fire(@Param("handlerName") String handlerName);

}
