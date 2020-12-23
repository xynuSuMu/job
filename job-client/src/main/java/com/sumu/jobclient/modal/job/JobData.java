package com.sumu.jobclient.modal.job;

import com.sumu.jobclient.handler.AbstractJobHandler;

import java.util.Map;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 20:01
 */
public class JobData {


    private Map<String, AbstractJobHandler> jobHandlers;

    public JobData(Map<String, AbstractJobHandler> jobHandlers) {
        this.jobHandlers = jobHandlers;
    }

    public Map<String, AbstractJobHandler> getJobHandlers() {
        return jobHandlers;
    }
}
