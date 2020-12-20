package com.sumu.jobclient.threadpool;

import com.alibaba.fastjson.JSONObject;
import com.sumu.jobclient.modal.threadpool.ThreadInfo;
import com.sumu.jobclient.zk.ThreadRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-15 14:17
 */
public class ThreadPoolExecutorManager extends ThreadPoolExecutor {

    private Logger LOG = LoggerFactory.getLogger(ThreadPoolExecutorManager.class);

    //运行线程数量
    private Map<Thread, ThreadInfo> threadMap = new ConcurrentHashMap<>();

    //todo:后期全部入库
    private List<ThreadInfo> history = new CopyOnWriteArrayList<>();

    //是否注册ZK
    private Boolean isRegister = false;

    //
    private ThreadRegister threadRegister = new ThreadRegister();

    //默认阀值
    private final long defaultThreshold = Long.MAX_VALUE;

    //初始化线程池方法时进行监控
    private final String LOCAL_VARIABLE_NAME = "init";

    //
    private volatile boolean openAlarm = true;

    //队列-预警阀值
    private long alarmThreshold;


    public ThreadPoolExecutorManager(int corePoolSize,
                                     int maximumPoolSize,
                                     BlockingQueue<Runnable> workQueue,
                                     ThreadFactory threadFactory,
                                     RejectedExecutionHandler rejectedExecutionHandler,
                                     long alarmThreshold) {
        //非核心线程无任务存活3分钟
        super(corePoolSize, maximumPoolSize,
                3,
                TimeUnit.MINUTES,
                workQueue,
                threadFactory,
                rejectedExecutionHandler);

        //预警阀值小于0，则为默认
        this.alarmThreshold = alarmThreshold < 0 ? defaultThreshold : alarmThreshold;
        //注册
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        //上一个调用栈
        String className = stacks[2].getClassName();
        String methodName = stacks[2].getMethodName();
        //初始化阶段-成员变量 方可注册
        if (methodName.contains(LOCAL_VARIABLE_NAME)) {
            threadRegister.register(className, this);
        } else {
            LOG.error("[ ThreadPoolExecutorManager Invaild ]");
        }
    }


    //运行中线程数量
    public long getRunThread() {
        //getTaskCount返回历史执行的任务 + 正在执行的任务数量
        return getTaskCount() - history.size() - (long) getQueue().size();
    }

    //运行中的线程列表
    public List<ThreadInfo> getRunThreadInfo() {
        Set<Map.Entry<Thread, ThreadInfo>> set = threadMap.entrySet();
        List<ThreadInfo> list = new ArrayList<>();
        for (Map.Entry<Thread, ThreadInfo> entry : set) {
            list.add(entry.getValue());
        }
        return list;
    }

    //获取历史线程执行情况
    public List<ThreadInfo> getHistoryThreadInfo() {
        return history;
    }

    //中断线程，todo:对于IO造成的阻塞或死循环内无检测中断，目前无很好解决方案
    public void interrupt(String threadName, long startTime) {
        Set<Map.Entry<Thread, ThreadInfo>> set = threadMap.entrySet();
        LOG.info("[ ThreadPoolManager ] interrupt thread:{}", threadName);
        for (Map.Entry<Thread, ThreadInfo> entry : set) {
            if (entry.getKey().getName().equals(threadName)
                    &&
                    entry.getValue().getStartTime().getTime() == startTime) {
                entry.getKey().interrupt();
            }
        }
    }

    public java.lang.management.ThreadInfo getThreadInfoByMXBean(long id) {
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        java.lang.management.ThreadInfo threadInfo = threadMXBean.getThreadInfo(id);
        return threadInfo;
    }

    @Override
    public void execute(Runnable command) {
        //队列
        if (openAlarm) {
            long queueSize = getQueue().size();
            if (queueSize + 1 > alarmThreshold) {
                //todo
                LOG.info("发送预警");
            }
        }
        super.execute(command);
    }

    //任务执行前
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        String threadName = t.getName();
        ThreadInfo threadInfo = new ThreadInfo();
        threadInfo.setId(t.getId());
        threadInfo.setThreadName(threadName);
        threadInfo.setStartTime(new Date(System.currentTimeMillis()));
        threadMap.put(t, threadInfo);
    }

    //任务执行完成后
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        Thread thread = Thread.currentThread();
        if (threadMap.containsKey(thread)) {
            ThreadInfo threadInfo = threadMap.get(thread);
            threadInfo.setEndTime(new Date(System.currentTimeMillis()));
            history.add(threadInfo);
            threadMap.remove(thread);
        }

    }

    public Boolean getRegister() {
        return isRegister;
    }

    public void setRegister(Boolean register) {
        isRegister = register;
    }


    public static class AlarmPolicy implements RejectedExecutionHandler {

        public AlarmPolicy() {
        }

        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            //todo:alarm
            System.out.println("[ALARM] Runable Rejected");
        }
    }

    public void setOpenAlarm(boolean openAlarm) {
        this.openAlarm = openAlarm;
    }
}

