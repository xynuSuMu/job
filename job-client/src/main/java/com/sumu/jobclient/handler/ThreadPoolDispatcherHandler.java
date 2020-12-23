package com.sumu.jobclient.handler;

import com.sumu.jobclient.common.Context;
import com.sumu.common.modal.rpc.RpcResult;
import com.sumu.jobclient.modal.threadpool.ThreadPoolManagerInfo;
import com.sumu.jobclient.threadpool.ThreadPoolExecutorManager;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-18 19:42
 */
public class ThreadPoolDispatcherHandler implements DispatcherHandler {

    private final String CLASS_NAME = "ClassName";

    @Override
    public RpcResult<?> handler(HttpServletRequest request) {

        String className = request.getParameter(CLASS_NAME);

        List<ThreadPoolExecutorManager> threadPoolExecutorManagers
                = Context.getThreadPoolManager(className);
        if (threadPoolExecutorManagers == null)
            return RpcResult.fail("[THREAD POOL] Not Found ThreadPoolExecutorManager");
        List<ThreadPoolManagerInfo> res = new ArrayList<>();
        for (ThreadPoolExecutorManager threadPoolExecutorManager : threadPoolExecutorManagers) {
            ThreadPoolManagerInfo threadPoolManagerInfo = new ThreadPoolManagerInfo();
            threadPoolManagerInfo
                    .setRunIngSize(String.valueOf(threadPoolExecutorManager.getRunThread()));
            threadPoolManagerInfo
                    .setCoreSize(String.valueOf(threadPoolExecutorManager.getCorePoolSize()));
            threadPoolManagerInfo
                    .setHistorySize(String.valueOf(threadPoolExecutorManager.getHistoryThreadInfo()
                            .size()));
            threadPoolManagerInfo.setRunThreadInfo(threadPoolExecutorManager.getRunThreadInfo());
            threadPoolManagerInfo.setHistoryThreadInfo(threadPoolExecutorManager.getHistoryThreadInfo());
            res.add(threadPoolManagerInfo);
        }
        return RpcResult.success(res);
    }

}
