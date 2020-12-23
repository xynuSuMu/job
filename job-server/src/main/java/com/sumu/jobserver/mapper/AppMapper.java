package com.sumu.jobserver.mapper;

import com.sumu.jobserver.modal.app.AppDO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 20:50
 */
public interface AppMapper {


    @Select("select id, app_code as appName from JOB_CUSTOM_APP")
    List<AppDO> getApps();

    @Select("select id, app_code as appName from JOB_CUSTOM_APP where id = #{id}")
    AppDO getAppById(@Param("id") int id);

    @Select("select id, app_code as appName from JOB_CUSTOM_APP where app_code = #{appName}")
    AppDO getByAppName(String appName);

    void insertAppCode(@Param("appCode") String appCode, @Param("zxid") long zxid);

}
