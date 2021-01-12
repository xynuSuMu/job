package com.sumu.jobserver.scheduler.interceptor.command.cmd.job.instance;

import com.sumu.jobserver.scheduler.interceptor.command.Command;
import com.sumu.jobserver.scheduler.interceptor.command.context.CommandContext;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.common.Page;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.instance.JobInstance;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.instance.JobInstanceEntityImpl;
import com.sumu.jobserver.scheduler.mapper.JobMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-12 16:48
 */
public class JobInstanceQueryCommand implements Command<List<JobInstance>> {

    private JobInstanceQuery jobInstanceQuery;

    public JobInstanceQueryCommand(JobInstanceQuery jobInstanceQuery) {
        this.jobInstanceQuery = jobInstanceQuery;
    }

    @Override
    public List<JobInstance> execute(CommandContext commandContext) {
        JobMapper jobMapper = commandContext.getSqlSession().getMapper(JobMapper.class);
        Page page = new Page((jobInstanceQuery.getPageIndex() - 1) * jobInstanceQuery.getPageSize(),
                jobInstanceQuery.getPageSize());
        List<JobInstanceEntityImpl> jobDefinitionEntities =
                jobMapper.jobInstanceList(
                        jobInstanceQuery.getJobInstanceEntity()
                        , page);
        List<JobInstance> res = new ArrayList<>();
        jobDefinitionEntities.stream().forEach(jobDefinitionEntity -> {
            res.add(jobDefinitionEntity);
        });
        return res;
    }
}
