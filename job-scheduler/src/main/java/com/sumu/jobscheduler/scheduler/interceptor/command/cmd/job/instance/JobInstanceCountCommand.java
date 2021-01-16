package com.sumu.jobscheduler.scheduler.interceptor.command.cmd.job.instance;

import com.sumu.jobscheduler.scheduler.interceptor.command.Command;
import com.sumu.jobscheduler.scheduler.interceptor.command.context.CommandContext;
import com.sumu.jobscheduler.scheduler.mapper.JobMapper;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-15 18:05
 */
public class JobInstanceCountCommand implements Command<Integer> {

    private JobInstanceQuery jobInstanceQuery;

    public JobInstanceCountCommand(JobInstanceQuery jobInstanceQuery) {
        this.jobInstanceQuery = jobInstanceQuery;
    }

    @Override
    public Integer execute(CommandContext commandContext) {
        JobMapper jobMapper = commandContext.getSqlSession().getMapper(JobMapper.class);
        int count = jobMapper.countInstance(jobInstanceQuery.getJobInstanceEntity());
        return count;
    }
}
