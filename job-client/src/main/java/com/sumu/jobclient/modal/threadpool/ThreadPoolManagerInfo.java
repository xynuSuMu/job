package com.sumu.jobclient.modal.threadpool;

import java.util.List;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-18 15:23
 */
public class ThreadPoolManagerInfo {

    private String coreSize;

    private String runIngSize;

    private String historySize;

    private List<ThreadInfo> runThreadInfo;

    private List<ThreadInfo> historyThreadInfo;

    public List<ThreadInfo> getRunThreadInfo() {
        return runThreadInfo;
    }

    public void setRunThreadInfo(List<ThreadInfo> runThreadInfo) {
        this.runThreadInfo = runThreadInfo;
    }

    public List<ThreadInfo> getHistoryThreadInfo() {
        return historyThreadInfo;
    }

    public void setHistoryThreadInfo(List<ThreadInfo> historyThreadInfo) {
        this.historyThreadInfo = historyThreadInfo;
    }

    public String getCoreSize() {
        return coreSize;
    }

    public void setCoreSize(String coreSize) {
        this.coreSize = coreSize;
    }

    public String getRunIngSize() {
        return runIngSize;
    }

    public void setRunIngSize(String runIngSize) {
        this.runIngSize = runIngSize;
    }

    public String getHistorySize() {
        return historySize;
    }

    public void setHistorySize(String historySize) {
        this.historySize = historySize;
    }
}
