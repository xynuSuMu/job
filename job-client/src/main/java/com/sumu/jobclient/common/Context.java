package com.sumu.jobclient.common;

import com.sumu.jobclient.threadpool.ThreadPoolExecutorManager;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-15 16:22
 */
public class Context {

    private static ApplicationContext applicationContext;

    private static Map<String, List<ThreadPoolExecutorManager>> threadManager = new ConcurrentHashMap();

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        Context.applicationContext = applicationContext;
    }

    public static Map<String, List<ThreadPoolExecutorManager>> getThreadManager() {
        return threadManager;
    }

    public static void addThreadPoolManger(String className, ThreadPoolExecutorManager threadPoolManager) {
        if (className == null || threadPoolManager == null)
            return;
        synchronized (Context.class) {
            if (!threadManager.containsKey(className)) {
                List<ThreadPoolExecutorManager> list = new ArrayList<>(3);
                list.add(threadPoolManager);
                threadManager.put(className, list);
            } else {
                List<ThreadPoolExecutorManager> list = threadManager.get(className);
                for (ThreadPoolExecutorManager temp : list) {
                    if (temp == threadPoolManager) {
                        return;
                    }
                }
                list.add(threadPoolManager);
            }
        }
    }

    public static List<ThreadPoolExecutorManager> getThreadPoolManager(String className) {
        if (className == null)
            return null;
        if (!threadManager.containsKey(className))
            return null;
        List<ThreadPoolExecutorManager> real = threadManager.get(className);
        return real;
    }
}
