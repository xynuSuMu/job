package com.sumu.jobserver.scheduler.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 15:43
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    public static <T> T getBean(Class<T> clz) {
        return context.getBean(clz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
