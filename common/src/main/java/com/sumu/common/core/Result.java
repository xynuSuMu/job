package com.sumu.common.core;


/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-22 19:09
 */
public class Result<T> {
    private boolean success;

    private T data;

    private String msg;

    private Integer code;

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.setSuccess(true);
        r.setData(data);
        return r;
    }

    public static <T> Result<T> fail(String msg) {
        return fail(0, msg);
    }

    public static <T> Result<T> fail(Integer code, String msg) {
        Result<T> r = new Result<>();
        r.setSuccess(false);
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
