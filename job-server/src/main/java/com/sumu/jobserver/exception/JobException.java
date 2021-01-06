package com.sumu.jobserver.exception;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-06 13:31
 */
public class JobException extends RuntimeException {

    private final Integer code;

    private JobException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public JobException(JobExceptionInfo info) {
        this(info.getCode(), info.getMsg());
    }
}
