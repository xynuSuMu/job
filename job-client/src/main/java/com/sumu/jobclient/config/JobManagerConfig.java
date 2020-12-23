package com.sumu.jobclient.config;


import com.sumu.jobclient.JobManager;
import com.sumu.jobclient.properties.AppProperties;
import com.sumu.jobclient.properties.JobProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-22 09:31
 */
@EnableConfigurationProperties({JobProperties.class, AppProperties.class})
public class JobManagerConfig {

    public JobManagerConfig() {

    }

    @Bean(
            initMethod = "start",
            destroyMethod = "destroy"
    )
    @ConditionalOnMissingBean({JobManager.class})
    public JobManager jobManager(JobProperties jobProperties, AppProperties appProperties) {
        return new JobManager(jobProperties, appProperties);
    }

}
