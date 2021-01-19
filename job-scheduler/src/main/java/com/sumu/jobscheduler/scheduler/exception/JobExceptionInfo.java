package com.sumu.jobscheduler.scheduler.exception;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-06 13:32
 */
public enum JobExceptionInfo {

    JOB_EXIST(1000, "triggerKey already exists! "),
    DAG_CIRCLE(2000, "dependency job is circle"),
    CHAIN(3000, "invalid command interceptor chain configuration:"),
    SHELL_JOB_INSERT_FAIL(4000, "SHELL JOB INSERT FAIL"),
    ;

    private Integer code;
    private String msg;

    JobExceptionInfo(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


}
