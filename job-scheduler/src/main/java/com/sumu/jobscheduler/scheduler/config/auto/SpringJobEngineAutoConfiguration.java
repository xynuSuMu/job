package com.sumu.jobscheduler.scheduler.config.auto;

import com.sumu.jobscheduler.scheduler.config.properties.JobProperties;
import com.sumu.jobscheduler.scheduler.core.monitor.JobRegisterHandler;
import com.sumu.jobscheduler.scheduler.core.monitor.ZkMonitor;
import com.sumu.jobscheduler.scheduler.core.schedule.JobDispatcher;
import com.sumu.jobscheduler.scheduler.core.schedule.JobSchedule;
import com.sumu.jobscheduler.scheduler.core.schedule.java.JobExecutor;
import com.sumu.jobscheduler.scheduler.core.schedule.shell.ShellExecutor;
import com.sumu.jobscheduler.scheduler.core.service.JobApplicationService;
import com.sumu.jobscheduler.scheduler.core.service.JobDefinitionService;
import com.sumu.jobscheduler.scheduler.core.service.JobInstanceService;
import com.sumu.jobscheduler.scheduler.core.service.WorkerService;
import com.sumu.jobscheduler.scheduler.core.store.PostSchedulerFactoryBean;
import org.quartz.Scheduler;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
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
            JobProperties jobProperties,
            DataSource dataSource,
            PlatformTransactionManager transactionManager) {
        SpringJobConfiguration springJobConfiguration = new SpringJobConfiguration(jobProperties);
        springJobConfiguration.setDataSource(dataSource);
        springJobConfiguration.setTransactionManager(transactionManager);
        return springJobConfiguration;
    }

    //修改Spring构造SchedulerFactory
    @Bean
    @ConditionalOnMissingBean
    public PostSchedulerFactoryBean postSchedulerFactoryBean() {
        return new PostSchedulerFactoryBean();
    }

    //调度器
    @Bean
    @ConditionalOnMissingBean
    public JobSchedule jobSchedule(
            Scheduler scheduler) {
        return new JobSchedule(scheduler);
    }

    @Bean
    @ConditionalOnMissingBean
    public JobFactoryBean jobFactoryBean(SpringJobConfiguration springJobConfiguration) {
        JobFactoryBean jobFactoryBean = new JobFactoryBean(springJobConfiguration);
        return jobFactoryBean;
    }

    //Service
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

    //监控、分发
    @Bean
    @ConditionalOnMissingBean
    public JobDispatcher jobDispatcher(JobDefinitionService jobDefinitionService) {
        return new JobDispatcher(jobDefinitionService);
    }

    @Bean
    @ConditionalOnMissingBean
    public JobRegisterHandler jobRegisterHandler(
            JobApplicationService jobApplicationService,
            WorkerService workerService
    ) {
        return new JobRegisterHandler(jobApplicationService, workerService);
    }

    @Bean
    @ConditionalOnMissingBean
    public ZkMonitor zkMonitor(JobProperties jobProperties, JobRegisterHandler registerHandler) {
        return new ZkMonitor(jobProperties, registerHandler);
    }

    //执行器
    @Bean
    @ConditionalOnMissingBean
    public JobExecutor jobExecutor(WorkerService workerService,
                                   JobDefinitionService jobDefinitionService,
                                   JobInstanceService jobInstanceService,
                                   JobDispatcher jobDispatcher) {
        return new JobExecutor(workerService, jobDefinitionService, jobInstanceService, jobDispatcher);
    }

    @Bean
    @ConditionalOnMissingBean
    public ShellExecutor shellExecutor(JobDefinitionService jobDefinitionService) {
        return new ShellExecutor(jobDefinitionService);
    }
}
