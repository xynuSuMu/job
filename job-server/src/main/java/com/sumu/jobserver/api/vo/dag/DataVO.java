package com.sumu.jobserver.api.vo.dag;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-06 14:45
 */
public class DataVO {
    private String name;
    private long x;
    private long y;

    public DataVO(String name, long x, long y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }
}
