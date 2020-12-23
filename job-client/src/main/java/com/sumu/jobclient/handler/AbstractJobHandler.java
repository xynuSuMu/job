package com.sumu.jobclient.handler;

import com.sumu.common.core.Result;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-04 10:05
 */
public abstract class AbstractJobHandler {

    public abstract <T> Result<T> execute(String var1) throws Exception;

}
