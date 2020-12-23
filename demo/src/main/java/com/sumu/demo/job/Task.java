package com.sumu.demo.job;

import com.sumu.common.core.Result;
import com.sumu.jobclient.annotation.JobHandler;
import com.sumu.jobclient.handler.AbstractJobHandler;

import java.util.Date;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-04 10:16
 */
@JobHandler("demoTask")
public class Task extends AbstractJobHandler {
    @Override
    public <T> Result<T> execute(String s) throws Exception {
        System.out.println("My Task");
        return Result.success();
    }
}
