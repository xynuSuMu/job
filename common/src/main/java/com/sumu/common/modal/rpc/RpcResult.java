package com.sumu.common.modal.rpc;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-18 21:38
 */
public class RpcResult<T> {

    public static final int DEFAULT_CODE = 0;

    private boolean success;

    private T data;

    private String msg;

    private Integer code;

    public static <T> RpcResult<T> success() {
        return success(null);
    }

    public static <T> RpcResult<T> success(T data) {
        RpcResult<T> r = new RpcResult<>();
        r.setSuccess(true);
        r.setData(data);
        return r;
    }

    public static <T> RpcResult<T> fail(String msg) {
        return fail(DEFAULT_CODE, msg);
    }

    public static <T> RpcResult<T> fail(Integer code, String msg) {
        RpcResult<T> r = new RpcResult<>();
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
