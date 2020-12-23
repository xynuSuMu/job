package com.sumu.jobclient.handler;

import com.sumu.common.util.rpc.RpcResult;
import com.sumu.jobclient.modal.job.JobData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 19:06
 */
public class JobDispatcherHandler implements DispatcherHandler {

    private final String HANDLER_NAME = "handlerName";

    private Logger LOG = LoggerFactory.getLogger(JobDispatcherHandler.class);

    private JobData jobData;

    public JobDispatcherHandler(JobData jobData) {
        this.jobData = jobData;
    }

    @Override
    public RpcResult<?> handler(HttpServletRequest request) {
        String handlerName = request.getParameter(HANDLER_NAME);
        LOG.info("[ JobDispatcherHandler ] handlerName:{}", handlerName);
        AbstractJobHandler jobHandler = jobData.getJobHandlers().get(handlerName);
        try {
            jobHandler.execute("");
            return RpcResult.success("Success");
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("[Job] Executor Error,{}", e);
            return RpcResult.fail("fail");
        }

    }
}
