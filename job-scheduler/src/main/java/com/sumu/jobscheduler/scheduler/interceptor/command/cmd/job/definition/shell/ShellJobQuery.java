package com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.definition.shell;

import com.sumu.jobscheduler.scheduler.core.service.impl.JobDefinitionServiceImpl;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.shell.ShellJobDefinition;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.shell.ShellJobDefinitionEntity;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.shell.ShellJobDefinitionEntityImpl;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-19 14:41
 */
public class ShellJobQuery {

    private JobDefinitionServiceImpl jobDefinitionService;


    private ShellJobDefinitionEntity shellJobDefinitionEntity;

    public ShellJobQuery(JobDefinitionServiceImpl jobDefinitionService) {
        this.jobDefinitionService = jobDefinitionService;

        shellJobDefinitionEntity = new ShellJobDefinitionEntityImpl();
    }

    public ShellJobQuery definitionId(int definitionId) {
        shellJobDefinitionEntity.setDefinitionID(definitionId);
        return this;
    }

    public ShellJobDefinitionEntity getShellJobDefinitionEntity() {
        return shellJobDefinitionEntity;
    }

    public ShellJobDefinition selectShellJob() {
        return jobDefinitionService.selectShellJob(this);
    }


}
