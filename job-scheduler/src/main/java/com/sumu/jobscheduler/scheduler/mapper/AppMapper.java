package com.sumu.jobscheduler.scheduler.mapper;

import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.app.AppEntity;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.app.AppEntityImpl;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 20:50
 */
@Repository
@Mapper
public interface AppMapper {

    List<AppEntityImpl> queryApp(@Param("appEntity") AppEntity appEntity);


    @Select("select id, app_code as appCode,zxid as zxID,update_time as updateTime " +
            " from JOB_CUSTOM_APP where app_code = #{appCode}")
    AppEntityImpl getByAppCode(@Param("appCode") String appCode);


    void insertAppCode(@Param("appCode") String appCode, @Param("zxid") long zxid);

}
