package com.sumu.jobclient.dispatcher;

import com.sumu.jobclient.handler.DispatcherHandler;
import com.sumu.jobclient.handler.ThreadPoolDispatcherHandler;
import com.sumu.jobclient.modal.rpc.RpcResult;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-17 16:29
 */
public class RequestDispatcher {

    private final String JOB_NOTIFY = "/job/notify";
    private final String THREAD_POOL_INFO = "/thread/pool/info";

    private Map<String, DispatcherHandler> urlHandlerMap = new HashMap<>();


    public RequestDispatcher() {
        urlHandlerMap.put(THREAD_POOL_INFO, new ThreadPoolDispatcherHandler());
    }

    public RpcResult<?> dispatcher(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (!urlHandlerMap.containsKey(uri)) {
            return RpcResult.fail("[ Handler ] no handler in '" + uri + "'");
        }
        DispatcherHandler dispatcherHandler = urlHandlerMap.get(uri);
        return dispatcherHandler.handler(request);
    }
}
