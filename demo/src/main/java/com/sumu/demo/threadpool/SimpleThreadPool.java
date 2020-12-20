package com.sumu.demo.threadpool;

import com.alibaba.fastjson.JSONObject;
import com.sumu.jobclient.modal.threadpool.ThreadInfo;
import com.sumu.jobclient.threadpool.ThreadPoolExecutorManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
            new ArrayBlockingQueue<>(1),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutorManager.AlarmPolicy(),
            2
    );


    public void test() {
        Lock lock = new ReentrantLock();
        lock.lock();
        threadPoolExecutor.execute(() -> {
            System.out.println("=====");

            lock.lock();
            System.out.println("+++++");
//            while (true) {
//                System.out.println(System.currentTimeMillis());
//            }
        });
        threadPoolExecutor.execute(() -> {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Test2");
        });
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadPoolExecutor.getThreadInfoByMXBean(1);
        threadPoolExecutor.interrupt("thread-pool-1", 1);

//        threadPoolExecutor.execute(() -> {
//            try {
//                Thread.sleep(6000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("Test3");
//        });
//        threadPoolExecutor.execute(() -> {
//            try {
//                Thread.sleep(6000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("Test4");
//        });
//
//
//        threadPoolExecutor.execute(() -> {
//            try {
//                Thread.sleep(6000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("Test5");
//        });

    }


}
