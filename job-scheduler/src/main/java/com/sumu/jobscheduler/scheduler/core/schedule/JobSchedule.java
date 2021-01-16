package com.sumu.jobscheduler.scheduler.core.schedule;

import com.sumu.jobscheduler.scheduler.exception.JobException;
import com.sumu.jobscheduler.scheduler.exception.JobExceptionInfo;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.quartz.TriggerBuilder.newTrigger;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 20:18
 */
public class JobSchedule {

    private Logger LOG = LoggerFactory.getLogger(JobSchedule.class);

    private Scheduler scheduler;

    public JobSchedule(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public void addJob(String jobName, String jobGroup, String cronExpr) throws SchedulerException {

        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);

        sureJobNotExist(triggerKey);

        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpr)
                .withMisfireHandlingInstructionDoNothing();

        CronTrigger cronTrigger = newTrigger()
                .withIdentity(triggerKey)
                .withSchedule(cronScheduleBuilder)
                .build();

        JobDetail jobDetail = JobBuilder.newJob(JobBean.class)
                .withIdentity(jobName, jobGroup).build();

        Date date = scheduler.scheduleJob(jobDetail, cronTrigger);
        LOG.info("add Job: jobDetail:{}, cronTrigger:{}, date:{}", jobDetail, cronTrigger, date);
    }

    public void removeIfExist(String jobName, String jobGroup) throws SchedulerException {

        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);

        boolean exists = scheduler.checkExists(triggerKey);

        if (!exists) {
//            throw new JobExistException("quartz job not exists! triggerKey = " + triggerKey);
            LOG.info("{} not exist", triggerKey);
        } else {
            scheduler.unscheduleJob(triggerKey);
        }
    }

    private void sureJobNotExist(TriggerKey triggerKey) throws SchedulerException {
        boolean exists = scheduler.checkExists(triggerKey);
        if (exists) {
            throw new JobException(JobExceptionInfo.JOB_EXIST);
        }
    }
}
