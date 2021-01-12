package com.sumu.jobserver.scheduler.interceptor.command.cmd.job.definition;

import com.sumu.jobserver.scheduler.interceptor.command.Command;
import com.sumu.jobserver.scheduler.interceptor.command.context.CommandContext;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.common.Page;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.definition.JobDefinition;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.definition.JobDefinitionEntityImpl;
import com.sumu.jobserver.scheduler.mapper.JobMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-12 11:15
 */
public class JobDefinitionQueryCommand implements Command<List<JobDefinition>> {


    private JobDefinitionQuery jobDefinitionQuery;


    public JobDefinitionQueryCommand(JobDefinitionQuery jobDefinitionQuery) {
        this.jobDefinitionQuery = jobDefinitionQuery;

    }

    @Override
    public List<JobDefinition> execute(CommandContext commandContext) {
        JobMapper jobMapper = commandContext.getSqlSession().getMapper(JobMapper.class);
        Page page = new Page((jobDefinitionQuery.getPageIndex() - 1) * jobDefinitionQuery.getPageSize(),
                jobDefinitionQuery.getPageSize());
        List<JobDefinitionEntityImpl> jobDefinitionEntities = jobMapper.getJobDefinitions(
                jobDefinitionQuery.getJobDefinitionEntity()
                , page);
        List<JobDefinition> res = new ArrayList<>();
        jobDefinitionEntities.stream().forEach(jobDefinitionEntity -> {
            res.add(jobDefinitionEntity);
        });
        return res;
    }
}
