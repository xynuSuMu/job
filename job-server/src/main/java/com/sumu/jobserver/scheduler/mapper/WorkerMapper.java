package com.sumu.jobserver.scheduler.mapper;

import com.sumu.jobserver.scheduler.interceptor.command.entity.data.worker.WorkerEntity;
import com.sumu.jobserver.scheduler.interceptor.command.entity.data.worker.WorkerEntityImpl;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 17:12
 */
@Repository
@Mapper
public interface WorkerMapper {

    void registerWorker(@Param("entity") WorkerEntity workerEntity);

    void unRegisterWorker(@Param("ip") String ip,
                          @Param("port") int port,
                          @Param("zxid") long zxid);

    List<WorkerEntityImpl> getJobWorker(@Param("workerEntity") WorkerEntity workerEntity);

}
