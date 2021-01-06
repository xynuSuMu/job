package com.sumu.jobserver.exception;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-06 13:32
 */
public enum JobExceptionInfo {

    JOB_EXIST(1000, "triggerKey already exists! "),
    DAG_CIRCLE(2000, "dependency job is circle"),
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
