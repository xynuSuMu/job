package com.sumu.jobserver.mapper;

import com.sumu.jobserver.api.vo.query.JobDefinitionQuery;
import com.sumu.jobserver.api.vo.query.JobInstanceQuery;
import com.sumu.jobserver.modal.job.JobDefinitionDO;
import com.sumu.jobserver.modal.job.JobInstanceDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 20:50
 */
public interface JobMapper {

    @Select("select count(id) from JOB_CUSTOM_JOB_DEFINITION where job_name  = #{jobName}")
    int countByJobName(@Param("jobName") String jobName);

    JobDefinitionDO getJobDefinitionByID(@Param("id") String id);

    List<JobDefinitionDO> jobDefinitionList(@Param("jobDefinitionQuery") JobDefinitionQuery jobDefinitionQuery);

    List<JobInstanceDO> jobInstanceList(@Param("jobInstanceQuery") JobInstanceQuery jobInstanceQuery);

    int insertJobDefinition(JobDefinitionDO jobDefinitionDO);

    int insertJobInstance(JobInstanceDO jobInstanceDO);
}