package com.sumu.jobscheduler.scheduler.core.schedule.config;

import com.sumu.jobscheduler.util.SpringContextUtils;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-24 18:03
 * @desc 无需实现 JobFactory，Spring对Quartz有扩展实现：AdaptableJobFactory。
 */
public class JobQuartzFactory implements JobFactory {

    @Override
    public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
        Method getJobDetail = null;
        Object jobDetail = null;
        try {
            getJobDetail = bundle.getClass().getMethod("getJobDetail");
            jobDetail = getJobDetail.invoke(bundle);
            Method getJobClass = jobDetail.getClass().getMethod("getJobClass");
            Class jobClass = (Class) getJobClass.invoke(jobDetail);
            Job o = (Job) SpringContextUtils.getBean(jobClass);
            return o;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
