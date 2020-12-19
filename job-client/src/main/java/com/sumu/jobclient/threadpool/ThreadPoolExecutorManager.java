package com.sumu.jobclient.threadpool;

import com.sumu.jobclient.common.Context;
import com.sumu.jobclient.modal.threadpool.ThreadInfo;
import com.sumu.jobclient.modal.threadpool.ThreadRegisterModal;
import com.sumu.jobclient.zk.ThreadRegister;
import com.sun.corba.se.spi.orbutil.threadpool.ThreadPoolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-15 14:17
 */
public class ThreadPoolExecutorManager extends ThreadPoolExecutor {

    protected Logger LOG = LoggerFactory.getLogger(ThreadPoolExecutorManager.class);

    private Map<Thread, ThreadInfo> threadMap = new ConcurrentHashMap<>();

    //todo:后期全部入库
    private List<ThreadInfo> history = new CopyOnWriteArrayList<>();

    private Boolean isRegister = false;

    private ThreadRegister threadRegister = new ThreadRegister();


    public ThreadPoolExecutorManager(int corePoolSize,
                                     int maximumPoolSize,
                                     long keepAliveTime,
                                     TimeUnit unit,
                                     BlockingQueue<Runnable> workQueue,
                                     ThreadFactory threadFactory,
                                     RejectedExecutionHandler rejectedExecutionHandler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, rejectedExecutionHandler);
        //注册
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        //上一个调用栈
        String className = stacks[2].getClassName();
        String methodName = stacks[2].getMethodName();
        //初始化阶段-成员变量 方可注册
        if (methodName.contains("init")) {
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

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        String threadName = t.getName();
        ThreadInfo threadInfo = new ThreadInfo();
        threadInfo.setThreadName(threadName);
        threadInfo.setStartTime(new Date(System.currentTimeMillis()));
        threadMap.put(t, threadInfo);
    }

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

    public static class DBPolicy implements RejectedExecutionHandler {

        public DBPolicy() {
        }

        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            //db
        }
    }

    public static class AlarmPolicy implements RejectedExecutionHandler {

        public AlarmPolicy() {
        }

        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            //alarm
        }
    }

}

