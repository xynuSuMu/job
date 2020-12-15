package com.sumu.jobclient.common;

import org.springframework.context.ApplicationContext;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-15 16:22
 */
public class Context {
    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        Context.applicationContext = applicationContext;
    }
}
