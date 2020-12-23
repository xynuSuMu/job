package com.sumu.jobclient.dispatcher;

import com.sumu.jobclient.handler.DispatcherHandler;
import com.sumu.jobclient.handler.JobDispatcherHandler;
import com.sumu.jobclient.handler.ThreadPoolDispatcherHandler;
import com.sumu.common.util.rpc.RpcResult;
import com.sumu.jobclient.modal.job.JobData;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import com.sumu.common.core.URLConstants;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-17 16:29
 */
public class RequestDispatcher {

    private Map<String, DispatcherHandler> urlHandlerMap = new HashMap<>();

    public RequestDispatcher(JobData jobData) {
        urlHandlerMap.put(URLConstants.THREAD_POOL_INFO, new ThreadPoolDispatcherHandler());
        urlHandlerMap.put(URLConstants.JOB_NOTIFY, new JobDispatcherHandler(jobData));
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
