package com.sumu.jobserver.scheduler.context;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-04 15:49
 */
//@Component
public class BeanDefinitionPostProcess implements BeanFactoryPostProcessor {


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

}

