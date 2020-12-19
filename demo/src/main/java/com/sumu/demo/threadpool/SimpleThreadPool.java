package com.sumu.demo.threadpool;

import com.alibaba.fastjson.JSONObject;
import com.sumu.jobclient.modal.threadpool.ThreadInfo;
import com.sumu.jobclient.threadpool.ThreadPoolExecutorManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-15 16:03
 */
@Service
public class SimpleThreadPool {

    //2个核心线程，其他任务进入无界限队列
    private ThreadPoolExecutorManager threadPoolExecutor = new ThreadPoolExecutorManager(
            2,
            2,
            0, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutorManager.DBPolicy()
    );

    public void test() {
        threadPoolExecutor.execute(() -> {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Test");
        });

        try {
            Thread.sleep(500);
            System.out.println("正在运行的线程数量" + threadPoolExecutor.getRunThread());
            System.out.println("空闲的线程数量" + (2 - threadPoolExecutor.getRunThread()));
            List<ThreadInfo> run = threadPoolExecutor.getRunThreadInfo();
            for (ThreadInfo threadInfo : run) {
                System.out.println(JSONObject.toJSONString(threadInfo));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        threadPoolExecutor.execute(() -> {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Test");
        });

        try {
            Thread.sleep(500);
            System.out.println("正在运行的线程数量" + threadPoolExecutor.getRunThread());
            System.out.println("空闲的线程数量" + (2 - threadPoolExecutor.getRunThread()));
            List<ThreadInfo> run = threadPoolExecutor.getRunThreadInfo();
            for (ThreadInfo threadInfo : run) {
                System.out.println(JSONObject.toJSONString(threadInfo));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(12000);
            System.out.println("正在运行的线程数量" + threadPoolExecutor.getRunThread());
            System.out.println("空闲的线程数量" + (2 - threadPoolExecutor.getRunThread()));
            List<ThreadInfo> historyThreadInfo = threadPoolExecutor.getHistoryThreadInfo();
            for (ThreadInfo threadInfo : historyThreadInfo) {
                System.out.println(JSONObject.toJSONString(threadInfo));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
