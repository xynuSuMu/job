package com.sumu.jobserver.mapper;

import com.sumu.jobserver.modal.worker.WorkerDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 17:12
 */
@Repository
public interface WorkerMapper {

    void registerWorker(@Param("appId") int appId,
                        @Param("hostname") String hostname,
                        @Param("ip") String ip,
                        @Param("port") int port,
                        @Param("zxid") long zxid);

    void unRegisterWorker(@Param("ip") String ip,
                          @Param("port") int port,
                          @Param("zxid") long zxid);

    List<WorkerDO> getRunWorkerByAppID(@Param("appID") int appID);

}
