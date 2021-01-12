package com.sumu.jobserver.scheduler.interceptor.command.cmd.job.definition.java;

import com.sumu.jobserver.scheduler.core.service.impl.JobDefinitionServiceImpl;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.definition.java.JavaJobDefinition;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.definition.java.JavaJobDefinitionEntity;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.definition.java.JavaJobDefinitionEntityImpl;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-12 14:08
 */
public class JavaJobQuery {

    private JavaJobDefinitionEntity javaJobDefinitionEntity;

    private JobDefinitionServiceImpl jobDefinitionService;

    public JavaJobQuery(JobDefinitionServiceImpl jobDefinitionService) {
        this.jobDefinitionService = jobDefinitionService;
        javaJobDefinitionEntity = new JavaJobDefinitionEntityImpl();
    }

    public JavaJobQuery definitionId(int definitionId) {
        javaJobDefinitionEntity.setDefinitionID(definitionId);
        return this;
    }


    public JavaJobQuery strategy(int strategy) {
        javaJobDefinitionEntity.setStrategy(strategy);
        return this;
    }


    public JavaJobQuery id(int id) {
        javaJobDefinitionEntity.setId(id);
        return this;
    }

    JavaJobDefinitionEntity getJavaJobDefinitionEntity() {
        return javaJobDefinitionEntity;
    }

    public JavaJobDefinition singleResult() {
        return jobDefinitionService.selectJavaJob(this);
    }
}
