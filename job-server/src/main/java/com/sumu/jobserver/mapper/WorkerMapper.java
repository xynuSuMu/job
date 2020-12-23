package com.sumu.jobserver.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 17:12
 */
public interface WorkerMapper {

    void registerWorker(@Param("appId") int appId,
                        @Param("hostname") String hostname,
                        @Param("ip") String ip,
                        @Param("port") int port,
                        @Param("zxid") long zxid);

    void unRegisterWorker(@Param("ip") String ip,
                          @Param("port") int port,
                          @Param("zxid") long zxid);

}
