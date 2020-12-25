package com.sumu.jobclient.handler;

import com.sumu.common.core.URLConstants;
import com.sumu.common.util.rpc.RpcResult;
import com.sumu.jobclient.modal.job.JobData;
import com.sumu.jobclient.modal.job.JobParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 19:06
 */
public class JobDispatcherHandler implements DispatcherHandler {

    private final String HANDLER_NAME = URLConstants.HANDLER_NAME;
    private final String SHARD_INDEX = URLConstants.SHARD_INDEX;
    private final String SHARD_TOTAL = URLConstants.SHARD_TOTAL;

    private Logger LOG = LoggerFactory.getLogger(JobDispatcherHandler.class);

    private JobData jobData;

    public JobDispatcherHandler(JobData jobData) {
        this.jobData = jobData;
    }

    @Override
    public RpcResult<?> handler(HttpServletRequest request) {
        String handlerName = request.getParameter(HANDLER_NAME);
        String shardIndex = request.getParameter(SHARD_INDEX);
        String shardTotal = request.getParameter(SHARD_TOTAL);
        LOG.info("[ JobDispatcherHandler ] handlerName:{}", handlerName);
        AbstractJobHandler jobHandler = jobData.getJobHandlers().get(handlerName);
        try {
            JobParam jobParam = new JobParam("", shardIndex, shardTotal);
            jobHandler.execute(jobParam);
            return RpcResult.success("Success");
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("[Job] Executor Error,{}", e);
            return RpcResult.fail("fail");
        }

    }
}
