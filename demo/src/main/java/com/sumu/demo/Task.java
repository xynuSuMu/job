package com.sumu.demo;

import com.sumu.jobclient.annotation.JobHandler;
import com.sumu.jobclient.handler.AbstractJobHandler;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-04 10:16
 */
@JobHandler
public class Task extends AbstractJobHandler {
    @Override
    public <T> T execute(String s) throws Exception {
        System.out.println("My Task");
        return null;
    }
}
