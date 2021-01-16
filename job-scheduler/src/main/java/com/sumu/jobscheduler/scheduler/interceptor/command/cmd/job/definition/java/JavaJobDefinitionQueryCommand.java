package com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.definition.java;

import com.sumu.jobscheduler.scheduler.interceptor.command.Command;
import com.sumu.jobscheduler.scheduler.interceptor.command.context.CommandContext;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.java.JavaJobDefinition;
import com.sumu.jobscheduler.scheduler.mapper.JobMapper;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-12 14:44
 */
public class JavaJobDefinitionQueryCommand implements Command<JavaJobDefinition> {

    private JavaJobQuery javaJobQuery;

    public JavaJobDefinitionQueryCommand(JavaJobQuery javaJobQuery) {
        this.javaJobQuery = javaJobQuery;
    }

    @Override
    public JavaJobDefinition execute(CommandContext commandContext) {
        JobMapper jobMapper = commandContext.getSqlSession().getMapper(JobMapper.class);
        JavaJobDefinition javaJobDefinition = jobMapper.getJavaJobDefinition(javaJobQuery.getJavaJobDefinitionEntity());
        return javaJobDefinition;
    }


}
