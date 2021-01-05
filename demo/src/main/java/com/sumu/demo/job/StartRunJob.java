package com.sumu.demo.job;

import com.sumu.jobclient.custom.JobHandlerCustomizer;
import com.sumu.jobclient.handler.AbstractJobHandler;
import com.sumu.jobclient.modal.job.JobData;
import com.sumu.jobclient.modal.job.JobParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-04 10:37
 */
@Component
public class StartRunJob implements JobHandlerCustomizer {


    private Logger LOG = LoggerFactory.getLogger(Task.class);

    @Override
    public void customize(JobData jobData) {
        Set<Map.Entry<String, AbstractJobHandler>> set = jobData.getJobHandlers().entrySet();
        for (Map.Entry<String, AbstractJobHandler> entry : set) {
            LOG.info("启动执行Job,{}", entry.getKey());
            try {
                entry.getValue().execute(new JobParam(null, null, null));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
