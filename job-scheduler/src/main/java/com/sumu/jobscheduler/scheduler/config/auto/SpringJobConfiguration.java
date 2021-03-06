package com.sumu.jobscheduler.scheduler.config.auto;

import com.sumu.jobscheduler.scheduler.config.properties.JobProperties;
import com.sumu.jobscheduler.scheduler.context.JobApplicationContext;
import com.sumu.jobscheduler.scheduler.core.service.JobApplicationService;
import com.sumu.jobscheduler.scheduler.core.service.JobDefinitionService;
import com.sumu.jobscheduler.scheduler.core.service.JobInstanceService;
import com.sumu.jobscheduler.scheduler.core.service.WorkerService;
import com.sumu.jobscheduler.scheduler.core.service.impl.*;
import com.sumu.jobscheduler.scheduler.exception.JobException;
import com.sumu.jobscheduler.scheduler.interceptor.*;
import com.sumu.jobscheduler.scheduler.interceptor.executor.CommandExecutor;
import com.sumu.jobscheduler.scheduler.interceptor.executor.CommandExecutorImpl;

import com.sumu.jobscheduler.util.MyBatisUtils;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.sumu.jobscheduler.scheduler.exception.JobExceptionInfo.CHAIN;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-08 09:14
 */
public class SpringJobConfiguration extends AbstractSpringJobConfiguration {

    private CommandExecutor commandExecutor;

    private JobProperties jobProperties;

    protected List<CommandInterceptor> commandInterceptors;

    //Service

    protected JobApplicationService jobApplicationService = new JobApplicationServiceImpl();

    protected WorkerService workerService = new WorkerServiceImpl();

    protected JobDefinitionService jobDefinitionService = new JobDefinitionServiceImpl();

    protected JobInstanceService jobInstanceService = new JobInstanceServiceImpl();

    public SpringJobConfiguration(JobProperties jobProperties) {
        this.jobProperties = jobProperties;
    }

    public JobEngine jobEngine() {
        this.init();
        return new JobEngineImpl(this);
    }

    public void init() {
        initJobApplicationContext();
        initSqlSessionFactory();
        initCommandExecutors();
        initService();
    }

    private void initJobApplicationContext() {
        JobApplicationContext.setJobProperties(jobProperties);
    }

    private void initCommandExecutors() {
        initCommandInterceptor();
        initCommandExecutor();
    }

    private void initCommandInterceptor() {
        if (this.commandInterceptors == null) {
            this.commandInterceptors = new ArrayList<>();
            //todo:自定义前置

            //事务拦截器
            this.commandInterceptors.add(new TransactionInterceptor(this.transactionManager));

            //上下文拦截器
            this.commandInterceptors.add(new CommandContextInterceptor(this));

            //todo:自定义后置

            //执行
            this.commandInterceptors.add(new CommandInvoker());
        }
    }

    private void initCommandExecutor() {
        int size = this.commandInterceptors.size();
        if (size > 0) {
            CommandInterceptor first = this.commandInterceptors.get(0);
            for (int i = 0; i < size - 1; ++i) {
                this.commandInterceptors.get(i)
                        .setNext(this.commandInterceptors.get(i + 1));
            }
            this.commandExecutor = new CommandExecutorImpl(first);
        } else {
            throw new JobException(CHAIN);
        }
    }

    private void initService() {
        this.initServiceCommandExecutor(this.jobApplicationService);
        this.initServiceCommandExecutor(this.workerService);
        this.initServiceCommandExecutor(this.jobDefinitionService);
        this.initServiceCommandExecutor(this.jobInstanceService);
    }

    private void initServiceCommandExecutor(Object service) {
        if (service instanceof ServiceImpl) {
            ((ServiceImpl) service).setCommandExecutor(this.commandExecutor);
        }
    }

    private void initSqlSessionFactory() {
        InputStream inputStream = null;
        try {
            inputStream = MyBatisUtils.getInputStream();
            Reader reader = new InputStreamReader(inputStream);
            Environment environment = new Environment("default",
                    new ManagedTransactionFactory(),
                    this.dataSource);
            Properties properties = new Properties();
            Configuration configuration = initMybatisConfiguration(environment, reader, properties);
            this.sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private Configuration initMybatisConfiguration(Environment environment, Reader reader, Properties properties) {
        XMLConfigBuilder parser = new XMLConfigBuilder(reader, "", properties);
        Configuration configuration = parser.getConfiguration();
        configuration.setEnvironment(environment);
        configuration = parser.parse();
        return configuration;
    }

    public JobApplicationService getJobApplicationService() {
        return jobApplicationService;
    }

    public JobDefinitionService getJobDefinitionService() {
        return jobDefinitionService;
    }

    public JobInstanceService getJobInstanceService() {
        return jobInstanceService;
    }

    public WorkerService getWorkerService() {
        return workerService;
    }


}
