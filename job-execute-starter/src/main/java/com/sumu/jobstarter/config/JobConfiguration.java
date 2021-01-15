package com.sumu.jobstarter.config;


import com.sumu.jobclient.JobManager;
import com.sumu.jobclient.custom.JobHandlerCustomizer;
import com.sumu.jobclient.handler.AbstractJobHandler;
import com.sumu.jobclient.properties.AppProperties;
import com.sumu.jobclient.properties.JobProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-04 09:08
 */
@ConditionalOnBean({AbstractJobHandler.class})
@ConditionalOnProperty(
        value = {"zk.enable"},
        havingValue = "true",
        matchIfMissing = true
)
@EnableConfigurationProperties({JobProperties.class, AppProperties.class})
public class JobConfiguration {
    public JobConfiguration() {

    }

    @Bean(
            initMethod = "start",
            destroyMethod = "destroy"
    )
    @ConditionalOnMissingBean({JobManager.class})
    public JobManager jobManager(JobProperties jobProperties, AppProperties appProperties, ObjectProvider<JobHandlerCustomizer> jobHandlerCustomizers) {
        return new JobManager(jobProperties, appProperties, jobHandlerCustomizers);
    }

}
