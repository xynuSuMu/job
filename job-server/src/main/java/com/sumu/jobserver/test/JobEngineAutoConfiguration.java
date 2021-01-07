package com.sumu.jobserver.test;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: chenlong
 * @Date: 2021/1/7 20:46
 * @Description:
 */
@Configuration
public class JobEngineAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SpringJobEngineConfiguration springJobEngineConfiguration() {
        SpringJobEngineConfiguration springJobEngineConfiguration = new SpringJobEngineConfiguration();
        return springJobEngineConfiguration;
    }


    @Bean
    @ConditionalOnMissingBean
    public JobFactoryBean jobFactoryBean(SpringJobEngineConfiguration springJobEngineConfiguration) {
        JobFactoryBean jobFactoryBean = new JobFactoryBean(springJobEngineConfiguration);
        return jobFactoryBean;
    }

    @Bean
    @ConditionalOnMissingBean
    public TestService testService(JobEngine jobEngine) {
        return jobEngine.getTestService();
    }

}
