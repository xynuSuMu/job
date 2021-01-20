package com.sumu.jobscheduler.scheduler.core.schedule.shell;

import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.sumu.jobscheduler.scheduler.core.schedule.AbstractJobExecutor;
import com.sumu.jobscheduler.scheduler.core.service.JobDefinitionService;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.JobDefinition;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.shell.Remote;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.shell.ShellJobDefinition;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.instance.JobInstance;
import com.sumu.jobscheduler.scheduler.modal.job.ExecutorResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-24 18:14
 */
public class ShellExecutor extends AbstractJobExecutor {

    private Logger LOG = LoggerFactory.getLogger(ShellExecutor.class);

    private JobDefinitionService jobDefinitionService;

    public ShellExecutor(JobDefinitionService jobDefinitionService) {
        this.jobDefinitionService = jobDefinitionService;
    }

    public void executorByQuartz(JobDefinition jobDefinitionDO, JobInstance jobInstance) {
        ShellJobDefinition shellJobDefinition = jobDefinitionService.createShellQuery()
                .definitionId(jobDefinitionDO.getId())
                .selectShellJob();
        doExecutor(shellJobDefinition, jobInstance);
    }

    private void doExecutor(ShellJobDefinition shellJobDefinition, JobInstance jobInstanceDO) {
        String result = shellJobDefinition.exec();
        ExecutorResult executorResult = new ExecutorResult(new Date(),
                shellJobDefinition.getHost(),
                result);
        updateInstance(jobInstanceDO.getId(), executorResult);
    }


}
