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
public class JavaJobBuilder {

    private JavaJobDefinitionEntity javaJobDefinitionEntity;

    private JobDefinitionServiceImpl jobDefinitionService;

    public JavaJobBuilder(JobDefinitionServiceImpl jobDefinitionService) {
        this.jobDefinitionService = jobDefinitionService;
        this.javaJobDefinitionEntity = new JavaJobDefinitionEntityImpl();
    }

    public JavaJobBuilder id(int id) {
        javaJobDefinitionEntity.setId(id);
        return this;
    }

    public JavaJobBuilder definitionID(int definitionID) {
        javaJobDefinitionEntity.setDefinitionID(definitionID);
        return this;
    }

    public JavaJobBuilder handlerName(String handlerName) {
        javaJobDefinitionEntity.setHandlerName(handlerName);
        return this;
    }

    public JavaJobBuilder strategy(int strategy) {
        javaJobDefinitionEntity.setStrategy(strategy);
        return this;
    }

    public JavaJobBuilder shardNum(int shardNum) {
        javaJobDefinitionEntity.setShardNum(shardNum);
        return this;
    }

    JavaJobDefinitionEntity getJavaJobDefinitionEntity() {
        return javaJobDefinitionEntity;
    }

    public JavaJobDefinition deploy() {
        return jobDefinitionService.createJavaJob(this);
    }

}
