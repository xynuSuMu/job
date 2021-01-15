package com.sumu.jobserver.scheduler.config.auto;

import com.sumu.jobserver.scheduler.config.properties.JobProperties;
import com.sumu.jobserver.scheduler.core.service.JobApplicationService;
import com.sumu.jobserver.scheduler.core.service.JobDefinitionService;
import com.sumu.jobserver.scheduler.core.service.JobInstanceService;
import com.sumu.jobserver.scheduler.core.service.WorkerService;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-08 09:16
 */
@Configuration
@AutoConfigureAfter(
        name = {"org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration"}
)
@EnableConfigurationProperties({JobProperties.class})
public class SpringJobEngineAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SpringJobConfiguration springJobConfiguration(
            DataSource dataSource,
            PlatformTransactionManager transactionManager) {
        SpringJobConfiguration springJobConfiguration = new SpringJobConfiguration();
        springJobConfiguration.setDataSource(dataSource);
        springJobConfiguration.setTransactionManager(transactionManager);
        return springJobConfiguration;
    }

    @Bean
    @ConditionalOnMissingBean
    public JobFactoryBean jobFactoryBean(SpringJobConfiguration springJobConfiguration) {
        JobFactoryBean jobFactoryBean = new JobFactoryBean(springJobConfiguration);
        return jobFactoryBean;
    }

    @Bean
    @ConditionalOnMissingBean
    public JobApplicationService jobApplicationService(JobEngine jobEngine) {
        return jobEngine.getJobApplicationService();
    }

    @Bean
    @ConditionalOnMissingBean
    public WorkerService workerService(JobEngine jobEngine) {
        return jobEngine.getWorkerService();
    }

    @Bean
    @ConditionalOnMissingBean
    public JobDefinitionService jobDefinitionService(JobEngine jobEngine) {
        return jobEngine.getJobDefinitionService();
    }

    @Bean
    @ConditionalOnMissingBean
    public JobInstanceService jobInstanceService(JobEngine jobEngine) {
        return jobEngine.getJobInstanceService();
    }


}
