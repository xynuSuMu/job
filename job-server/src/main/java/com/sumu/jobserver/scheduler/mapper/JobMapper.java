package com.sumu.jobserver.scheduler.mapper;


import com.sumu.jobserver.scheduler.interceptor.command.entity.data.common.Page;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.definition.JobDefinitionEntity;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.definition.JobDefinitionEntityImpl;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.definition.java.JavaJobDefinition;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.definition.java.JavaJobDefinitionEntity;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.instance.JobInstanceEntity;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.instance.JobInstanceEntityImpl;

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

    Integer insertJobDefinition(JobDefinitionEntity jobDefinition);

    Integer updateJobDefinition(@Param("jobDefinition") JobDefinitionEntity jobDefinition);

    int insertJavaJobDefinition(JavaJobDefinitionEntity javaJobDefinitionEntity);

    Integer count(@Param("jobDefinition") JobDefinitionEntity jobDefinition);

    List<JobDefinitionEntityImpl> getJobDefinitions(
            @Param("jobDefinition") JobDefinitionEntity jobDefinition,
            @Param("page") Page page);

    JavaJobDefinition getJavaJobDefinition(@Param("javaJobDefinition") JavaJobDefinition javaJobDefinition);


    int removeJobDefinition(@Param("id") int id);

    int insertJobInstance(@Param("jobInstanceEntity") JobInstanceEntity jobInstanceEntity);

    int updateJobInstance(@Param("jobInstanceEntity") JobInstanceEntity jobInstanceEntity);


    List<JobInstanceEntityImpl> jobInstanceList(@Param("jobInstanceEntity") JobInstanceEntity jobInstanceEntity,
                                                @Param("page") Page page
    );

    Integer countInstance(@Param("jobInstanceEntity") JobInstanceEntity jobInstanceEntity);


}
