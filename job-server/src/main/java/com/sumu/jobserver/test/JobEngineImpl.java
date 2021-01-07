package com.sumu.jobserver.test;

/**
 * @Auther: chenlong
 * @Date: 2021/1/7 21:13
 * @Description:
 */
public class JobEngineImpl implements JobEngine {

    private TestService testService;

    public JobEngineImpl(SpringJobEngineConfiguration springJobEngineConfiguration) {
        this.testService = springJobEngineConfiguration.getTestService();
    }

    @Override
    public TestService getTestService() {
        return testService;
    }
}
