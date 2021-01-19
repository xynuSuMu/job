package com.sumu.jobscheduler.scheduler.mapper;


import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.common.Page;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.JobDefinitionEntity;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.JobDefinitionEntityImpl;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.java.JavaJobDefinition;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.java.JavaJobDefinitionEntity;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.shell.ShellJobDefinition;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.shell.ShellJobDefinitionEntity;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.instance.JobInstanceEntity;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.instance.JobInstanceEntityImpl;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 20:50
 */
@Repository
@Mapper
public interface JobMapper {

    //insert Job Definition
    Integer insertJobDefinition(JobDefinitionEntity jobDefinition);

    //insert Java Job
    Integer insertJavaJobDefinition(JavaJobDefinitionEntity javaJobDefinitionEntity);

    //insert Shell Job
    Integer insertShellJobDefinition(ShellJobDefinitionEntity shellJobDefinitionEntity);

    //insert Job Instance
    Integer insertJobInstance(@Param("jobInstanceEntity") JobInstanceEntity jobInstanceEntity);

    //update Job Definition
    Integer updateJobDefinition(@Param("jobDefinition") JobDefinitionEntity jobDefinition);

    //count Job Definition
    Integer count(@Param("jobDefinition") JobDefinitionEntity jobDefinition);

    //Query Job Definition
    List<JobDefinitionEntityImpl> getJobDefinitions(
            @Param("jobDefinition") JobDefinitionEntity jobDefinition,
            @Param("page") Page page);

    JavaJobDefinition getJavaJobDefinition(@Param("javaJobDefinition") JavaJobDefinition javaJobDefinition);

    ShellJobDefinition getShellJobDefinition(@Param("shellJobDefinition") ShellJobDefinition shellJobDefinition);

    Integer removeJobDefinition(@Param("id") int id);


    Integer updateJobInstance(@Param("jobInstanceEntity") JobInstanceEntity jobInstanceEntity);


    List<JobInstanceEntityImpl> jobInstanceList(@Param("jobInstanceEntity") JobInstanceEntity jobInstanceEntity,
                                                @Param("page") Page page
    );

    Integer countInstance(@Param("jobInstanceEntity") JobInstanceEntity jobInstanceEntity);


}
