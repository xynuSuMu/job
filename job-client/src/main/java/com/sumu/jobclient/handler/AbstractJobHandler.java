package com.sumu.jobclient.handler;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-04 10:05
 */
public abstract class AbstractJobHandler {

    public abstract <T> T execute(String var1) throws Exception;

}
