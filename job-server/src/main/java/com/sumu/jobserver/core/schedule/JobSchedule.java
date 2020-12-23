package com.sumu.jobserver.core.schedule;

import com.sumu.jobserver.exception.JobExistException;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Date;

import static org.quartz.TriggerBuilder.newTrigger;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 20:18
 */
@Component
public class JobSchedule {

    private Logger LOG = LoggerFactory.getLogger(JobSchedule.class);

    @Autowired
    private Scheduler scheduler;

    public void addJob(String jobName, String jobGroup, String cronExpr) throws SchedulerException {


        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);

        sureJobNotExist(triggerKey);

        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpr)
                .withMisfireHandlingInstructionDoNothing();
        CronTrigger cronTrigger = newTrigger()
                .withIdentity(triggerKey)
                .withSchedule(cronScheduleBuilder).build();

        JobDetail jobDetail = JobBuilder.newJob(JobBean.class)
                .withIdentity(jobName, jobGroup).build();

        Date date = scheduler.scheduleJob(jobDetail, cronTrigger);
        LOG.info("add Job: jobDetail:{}, cronTrigger:{}, date:{}", jobDetail, cronTrigger, date);
    }

    private void sureJobNotExist(TriggerKey triggerKey) throws SchedulerException {
        boolean exists = scheduler.checkExists(triggerKey);
        if (exists) {
            throw new JobExistException("quartz job already exists! triggerKey = " + triggerKey);
        }
    }
}