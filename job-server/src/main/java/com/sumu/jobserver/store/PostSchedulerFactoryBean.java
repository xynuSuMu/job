package com.sumu.jobserver.store;


import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.simpl.SimpleThreadPool;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.scheduling.quartz.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;


/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-01 20:20
 */
@Component
public class PostSchedulerFactoryBean implements SchedulerFactoryBeanCustomizer {

    private SchedulerFactory schedulerFactory;

    private Class<? extends SchedulerFactory> schedulerFactoryClass = StdSchedulerFactory.class;

    @Override
    public void customize(SchedulerFactoryBean schedulerFactoryBean) {
        //自定义SchedulerFactory
        schedulerFactory = BeanUtils.instantiateClass(this.schedulerFactoryClass);
        try {
            initSchedulerFactory(schedulerFactoryBean, (StdSchedulerFactory) schedulerFactory);
        } catch (SchedulerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        schedulerFactoryBean.setSchedulerFactory(schedulerFactory);
    }

    private void initSchedulerFactory(SchedulerFactoryBean schedulerFactoryBean, StdSchedulerFactory schedulerFactory) throws SchedulerException, IOException, NoSuchFieldException, IllegalAccessException {

        Properties mergedProps = new Properties();
        Field field = SchedulerAccessor.class.getDeclaredField("resourceLoader");
        field.setAccessible(true);
        if (field.get(schedulerFactoryBean) != null) {
            mergedProps.setProperty("org.quartz.scheduler.classLoadHelper.class", ResourceLoaderClassLoadHelper.class.getName());
        }
        Field field2 = schedulerFactoryBean.getClass().getDeclaredField("taskExecutor");
        field2.setAccessible(true);
        if (field2.get(schedulerFactoryBean) != null) {
            mergedProps.setProperty("org.quartz.threadPool.class", LocalTaskExecutorThreadPool.class.getName());
        } else {
            mergedProps.setProperty("org.quartz.threadPool.class", SimpleThreadPool.class.getName());
            mergedProps.setProperty("org.quartz.threadPool.threadCount", Integer.toString(10));
        }
        Field field3 = schedulerFactoryBean.getClass().getDeclaredField("configLocation");
        field3.setAccessible(true);
        Resource configLocation = (Resource) field3.get(schedulerFactoryBean);
        if (configLocation != null) {

            PropertiesLoaderUtils.fillProperties(mergedProps, configLocation);
        }
        Field field4 = schedulerFactoryBean.getClass().getDeclaredField("quartzProperties");
        field4.setAccessible(true);
        Properties properties = (Properties) field4.get(schedulerFactoryBean);
        CollectionUtils.mergePropertiesIntoMap(properties, mergedProps);
        //修改 jobStore.class
        mergedProps.setProperty("org.quartz.jobStore.class", SelfJobStoreTX.class.getName());

        Field field5 = schedulerFactoryBean.getClass().getDeclaredField("dataSource");
        field5.setAccessible(true);
        if (field5.get(schedulerFactoryBean) != null) {
            mergedProps.setProperty("org.quartz.jobStore.class", SelfJobStoreTX.class.getName());
        }
        schedulerFactory.initialize(mergedProps);
    }

}
