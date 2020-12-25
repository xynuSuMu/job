package com.sumu.demo.job;

import com.sumu.common.core.Result;
import com.sumu.jobclient.annotation.JobHandler;
import com.sumu.jobclient.handler.AbstractJobHandler;
import com.sumu.jobclient.modal.job.JobParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-04 10:16
 */
@JobHandler("demoTask")
public class Task extends AbstractJobHandler {

    private Logger LOG = LoggerFactory.getLogger(Task.class);

    @Override
    public <T> Result<T> execute(JobParam jobParam) throws Exception {

        LOG.info("[Shard] index = {},total={}", jobParam.getShardIndex(), jobParam.getShardTotal());
        LOG.info(" Demo Task");
        //测试超时
//        Thread.sleep(9999);
//        throw new RuntimeException("测试异常");
        return Result.success();
    }
}
