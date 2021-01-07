package com.sumu.jobserver.test;

/**
 * @Auther: chenlong
 * @Date: 2021/1/7 20:47
 * @Description:
 */
public class SpringJobEngineConfiguration {

    protected TestService testService = new TestServiceImpl();

    public TestService getTestService() {
        return testService;
    }

    public JobEngine buildProcessEngine() {
        JobEngine jobEngine = new JobEngineImpl(this);
        return jobEngine;
    }
}
