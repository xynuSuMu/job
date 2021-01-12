package com.sumu.jobserver.scheduler.interceptor.command.cmd.job.definition.java;

import com.sumu.jobserver.scheduler.interceptor.command.Command;
import com.sumu.jobserver.scheduler.interceptor.command.context.CommandContext;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.definition.java.JavaJobDefinition;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.definition.java.JavaJobDefinitionEntity;
import com.sumu.jobserver.scheduler.mapper.JobMapper;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-12 14:19
 */
public class CreateJavaJobDefinitionCommand implements Command<JavaJobDefinition> {

    private JavaJobBuilder javaJobBuilder;

    public CreateJavaJobDefinitionCommand(JavaJobBuilder javaJobBuilder) {
        this.javaJobBuilder = javaJobBuilder;
    }

    @Override
    public JavaJobDefinition execute(CommandContext commandContext) {
        JobMapper jobMapper = commandContext.getSqlSession().getMapper(JobMapper.class);
        JavaJobDefinitionEntity javaJobDefinition = javaJobBuilder.getJavaJobDefinitionEntity();
        jobMapper.insertJavaJobDefinition(javaJobDefinition);
        return javaJobDefinition;
    }
}
