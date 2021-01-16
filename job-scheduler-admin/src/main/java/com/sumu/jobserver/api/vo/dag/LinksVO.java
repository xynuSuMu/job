package com.sumu.jobserver.api.vo.dag;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-06 14:45
 */
public class LinksVO {
    private int source;
    private int target;

    public LinksVO(int source, int target) {
        this.source = source;
        this.target = target;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }
}
