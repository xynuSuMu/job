package com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.definition;

import com.sumu.jobscheduler.scheduler.interceptor.command.Command;
import com.sumu.jobscheduler.scheduler.interceptor.command.context.CommandContext;
import com.sumu.jobscheduler.scheduler.mapper.JobMapper;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-12 11:15
 */
public class JobDefinitionCountCommand implements Command<Integer> {

    private JobDefinitionQuery jobDefinitionQuery;

    public JobDefinitionCountCommand(JobDefinitionQuery jobDefinitionQuery) {
        this.jobDefinitionQuery = jobDefinitionQuery;
    }

    @Override
    public Integer execute(CommandContext commandContext) {
        JobMapper jobMapper = commandContext.getSqlSession().getMapper(JobMapper.class);
        return jobMapper.count(jobDefinitionQuery.getJobDefinitionEntity());
    }

}
