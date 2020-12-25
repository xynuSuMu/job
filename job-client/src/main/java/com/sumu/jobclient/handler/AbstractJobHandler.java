package com.sumu.jobclient.handler;

import com.sumu.common.core.Result;
import com.sumu.jobclient.modal.job.JobParam;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-04 10:05
 */
public abstract class AbstractJobHandler {

    public void post() {

    }

    public abstract <T> Result<T> execute(JobParam jobParam) throws Exception;

    public void destroy() {

    }

}
