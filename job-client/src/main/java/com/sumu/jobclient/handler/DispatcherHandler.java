package com.sumu.jobclient.handler;

import com.sumu.common.modal.rpc.RpcResult;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-18 19:34
 */
public interface DispatcherHandler {

    RpcResult<?> handler(HttpServletRequest request);

}
