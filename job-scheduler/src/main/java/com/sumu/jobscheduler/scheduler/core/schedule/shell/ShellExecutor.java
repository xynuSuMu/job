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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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

        StringBuilder stringBuilder = new StringBuilder();
        try {
            ChannelExec channel;
            InputStream input;
            Session session = shellJobDefinition.getSession();
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand("sh " + shellJobDefinition.getDirectory() + shellJobDefinition.getFile() + " " + shellJobDefinition.getParam());
            input = channel.getInputStream();
            channel.connect(10000);
            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(input));
                String inputLine;
                while ((inputLine = inputReader.readLine()) != null) {
//                    System.out.println(inputLine);
                    stringBuilder.append(inputLine + "\n");
                }
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateInstance(jobInstanceDO.getId(), 1, stringBuilder.toString());
    }


}
