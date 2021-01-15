package com.sumu.jobserver.api.vo;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-15 16:10
 */
public class Page<T> {

    private int total;

    private int current;

    private T result;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
