package com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.definition.shell;

import com.sumu.jobscheduler.scheduler.core.service.impl.JobDefinitionServiceImpl;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.shell.ShellJobDefinitionEntity;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.shell.ShellJobDefinitionEntityImpl;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-19 14:41
 */
public class ShellJobBuilder {

    private JobDefinitionServiceImpl jobDefinitionService;

    private ShellJobDefinitionEntity shellJobDefinitionEntity;

    public ShellJobBuilder(JobDefinitionServiceImpl jobDefinitionService) {
        this.jobDefinitionService = jobDefinitionService;
        shellJobDefinitionEntity = new ShellJobDefinitionEntityImpl();
    }

    public ShellJobDefinitionEntity getShellJobDefinitionEntity() {
        return shellJobDefinitionEntity;
    }

    public ShellJobBuilder id(int id) {
        shellJobDefinitionEntity.setId(id);
        return this;
    }

    public ShellJobBuilder definitionID(int definitionId) {
        shellJobDefinitionEntity.setDefinitionID(definitionId);
        return this;
    }

    public ShellJobBuilder user(String user) {
        shellJobDefinitionEntity.setUser(user);
        return this;
    }


    public ShellJobBuilder host(String host) {
        shellJobDefinitionEntity.setHost(host);
        return this;
    }


    public ShellJobBuilder port(int port) {
        shellJobDefinitionEntity.setPort(port);
        return this;
    }


    public ShellJobBuilder pwd(String pwd) {
        shellJobDefinitionEntity.setPassword(pwd);
        return this;
    }


    public ShellJobBuilder directory(String directory) {
        shellJobDefinitionEntity.setDirectory(directory);
        return this;
    }


    public ShellJobBuilder file(String file) {
        shellJobDefinitionEntity.setFile(file);
        return this;
    }

    public ShellJobBuilder param(String param) {
        shellJobDefinitionEntity.setParam(param);
        return this;
    }

    public Boolean create() {
        return jobDefinitionService.createShellJob(this);
    }


}
