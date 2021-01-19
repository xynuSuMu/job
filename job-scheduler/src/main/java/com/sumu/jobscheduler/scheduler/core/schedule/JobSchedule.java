package com.sumu.jobscheduler.scheduler.core.schedule;

import com.sumu.jobscheduler.scheduler.exception.JobException;
import com.sumu.jobscheduler.scheduler.exception.JobExceptionInfo;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;

import javax.sql.DataSource;
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

    private DataSource dataSource;

    private QuartzProperties quartzProperties;

    private final String tablePrefix;

    private final String TABLE = "TRIGGERS";

    private String UPDATE_TRIGGER = "UPDATE {0} set JOB_APP = {1} where TRIGGER_NAME = {2} and TRIGGER_GROUP = {3}";


    public JobSchedule(DataSource dataSource, QuartzProperties quartzProperties, Scheduler scheduler) {
        this.quartzProperties = quartzProperties;
        this.dataSource = dataSource;
        this.scheduler = scheduler;
        this.tablePrefix = quartzProperties
                .getProperties()
                .getOrDefault("org.quartz.jobStore.tablePrefix", "QRTZ_");

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

        JobDetail jobDetail = JobBuilder
                .newJob(JobBean.class)
                .withIdentity(jobName, jobGroup).build();

        Date date = scheduler.scheduleJob(jobDetail, cronTrigger);
        LOG.info("add Job: jobDetail:{}, cronTrigger:{}, date:{}", jobDetail, cronTrigger, date);
    }

    public void removeIfExist(String jobName, String jobGroup) throws SchedulerException {

        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);

        boolean exists = scheduler.checkExists(triggerKey);

        if (!exists) {
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
