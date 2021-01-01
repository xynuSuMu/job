package com.sumu.demo.source.code;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-31 15:50
 * QuartzSchedulerThread 调度线程源码
 */
public class QuartzSchedulerThread_Annotation {

  /**
    public void run() {
        int acquiresFailed = 0;
        //**********判断是否应该结束调度
        while (!halted.get()) {
            try {
                // check if we're supposed to pause...
                //**********这里的sigLock，sig应该是signal(信号)的简写
                synchronized (sigLock) {
                    //**********判断是否应该暂停调度，如果暂停则不断在循环中阻塞
                    //**********如暂停状态被外部环境修改，则线程会被立即唤醒并退出循环
                    while (paused && !halted.get()) {
                        try {
                            // wait until togglePause(false) is called...
                            sigLock.wait(1000L);
                        } catch (InterruptedException ignore) {
                        }

                        // reset failure counter when paused, so that we don't
                        // wait again after unpausing
                        acquiresFailed = 0;
                    }

                    if (halted.get()) {
                        break;
                    }
                }

                // wait a bit, if reading from job store is consistently
                // failing (e.g. DB is down or restarting)..
                //**********在前几次的循环中如果触发器的读取出现问题，
                //**********则可能是数据库重启一类的原因引发的故障
                if (acquiresFailed > 1) {
                    try {
                        long delay = computeDelayForRepeatedErrors(qsRsrcs.getJobStore(), acquiresFailed);
                        Thread.sleep(delay);
                    } catch (Exception ignore) {
                    }
                }

                //**********查询可用于执行任务(job)的工作线程数量，
                //**********若线程池中暂无可用线程则blockForAvailableThreads方法将会阻塞
                int availThreadCount = qsRsrcs.getThreadPool().blockForAvailableThreads();
                if(availThreadCount > 0) { // will always be true, due to semantics of blockForAvailableThreads...
                    //**********这个if分支查询到可用的工作线程，从JobStore中获取一批即将执行的触发器
                    //**********这里的JobStore存储介质可以是数据库、也可以是内存
                    List<OperableTrigger> triggers;

                    long now = System.currentTimeMillis();

                    clearSignaledSchedulingChange();
                    try {
                        triggers = qsRsrcs.getJobStore().acquireNextTriggers(
                                now + idleWaitTime, Math.min(availThreadCount, qsRsrcs.getMaxBatchSize()), qsRsrcs.getBatchTimeWindow());

                        //**********acquireNextTriggers方法获取一批即将执行的触发器
                        //**********参数idleWaitTime默认为30s,即当前时间后30s内即将被触发执行的触发器就会被取出


                        //**********此外在acquireNextTriggers方法内部还有一个参数misfireThreshold
                        //**********misfireThreshold是一个时间范围，用于判定触发器是否延时触发
                        //**********misfireThreshold默认值是60秒，它相对的实际意义就是:
                        //**********在当前时间的60秒之前本应执行但尚未执行的触发器不被认为是延迟触发,
                        //**********这些触发器同样会被acquireNextTriggers发现
                        //**********有时由于工程线程繁忙、程序重启等原因，原本预定要触发的任务可能延迟
                        //**********我们可以在每个触发器中可以设置MISFIRE_INSTRUCTION,用于指定延迟触发后使用的策略
                        //**********举例，对于CronTrigger,延迟处理的策略主要有3种：
                        //**********（1）一个触发器无论延迟多少次，这些延迟都会被程序尽可能补回来
                        //**********（2）检测到触发器延迟后，该触发器会在尽可能短的时间内被立即执行一次(只有一次)，然后恢复正常
                        //**********（3）检测到延迟后不采取任何动作，触发器以现在时间为基准，根据自身的安排等待下一次被执行或停止，
                        //**********     比如有些触发器只执行一次，一旦延迟后，该触发器也不会被触发



                        //**********关于触发器是否延迟的判定由一个叫MisfireHandler的线程独立负责，
                        //**********它会判定并影响延迟触发器的下一次触发，但不会真正进行触发的动作，
                        //**********触发的工作将统一交由QuartzSchedulerThread即本线程处理
                        //**********如果判定一个触发器延迟，则根据策略修改触发器的下一次执行时间或直接停止触发器
                        //**********所以这些延迟触发器被MisfireHandler处理后若仍有下次执行机会，就同样会在其触发时间被发现并触发
                        //**********需要注意的是MisfireHandler只会处理延迟策略不为上述第(1)类的触发器
                        //**********第(1)类触发器在延迟后，一旦获取到资源就可触发，这个过程不需被修改下次执行时间就可完成




                        //**********acquireNextTriggers方法最后一个参数batchTimeWindow，这个参数默认是0，同样是一个时间范围
                        //**********acquireNextTriggers可以每次取出一批触发器，但默认情况下这批触发器只会有一个
                        //**********但是有时候我们对任务执行的时间要求不严格时，就可以让两个执行时间距离较近的触发器同时被取出执行

                        //**********举例，有两个触发器分别是10:00:00和10:00:05执行
                        //**********此时如果将batchTimeWindow调整为大于等于5000毫秒，maxBatchSize数量大于等于2，
                        //**********且拥有足够的线程时,这两个触发器就有可能会在预定时间10:00:00被同时执行
                        acquiresFailed = 0;
                        if (log.isDebugEnabled())
                            log.debug("batch acquisition of " + (triggers == null ? 0 : triggers.size()) + " triggers");
                    } catch (JobPersistenceException jpe) {
                        if (acquiresFailed == 0) {
                            qs.notifySchedulerListenersError(
                                    "An error occurred while scanning for the next triggers to fire.",
                                    jpe);
                        }
                        if (acquiresFailed < Integer.MAX_VALUE)
                            acquiresFailed++;
                        continue;
                    } catch (RuntimeException e) {
                        if (acquiresFailed == 0) {
                            getLog().error("quartzSchedulerThreadLoop: RuntimeException "
                                    +e.getMessage(), e);
                        }
                        if (acquiresFailed < Integer.MAX_VALUE)
                            acquiresFailed++;
                        continue;
                    }

                    if (triggers != null && !triggers.isEmpty()) {

                        now = System.currentTimeMillis();
                        long triggerTime = triggers.get(0).getNextFireTime().getTime();
                        long timeUntilTrigger = triggerTime - now;

                        while(timeUntilTrigger > 2) {
                            //**********在该while循环体中，被取出的触发器会阻塞等待到预定时间被触发
                            //**********这里用了阻塞，因为当外部环境对触发器做了调整或者新增时，会对线程进行唤醒
                            //**********在阻塞被唤醒后，会有相关的逻辑判断是否应该重新取出触发器来执行
                            //**********比如当前时间是10:00:00，在上述逻辑中已经取出了10:00:05需要执行的触发器
                            //**********此时如果新增了一个10:00:03的触发器，则可能需要丢弃10:00:05的，再取出10:00:03的
                            synchronized (sigLock) {
                                if (halted.get()) {
                                    break;
                                }
                                if (!isCandidateNewTimeEarlierWithinReason(triggerTime, false)) {
                                    try {
                                        // we could have blocked a long while
                                        // on 'synchronize', so we must recompute
                                        now = System.currentTimeMillis();
                                        timeUntilTrigger = triggerTime - now;
                                        if(timeUntilTrigger >= 1)
                                            sigLock.wait(timeUntilTrigger);
                                    } catch (InterruptedException ignore) {
                                    }
                                }
                            }
                            if(releaseIfScheduleChangedSignificantly(triggers, triggerTime)) {
                                break;
                            }
                            now = System.currentTimeMillis();
                            timeUntilTrigger = triggerTime - now;
                        }

                        // this happens if releaseIfScheduleChangedSignificantly decided to release triggers
                        if(triggers.isEmpty())
                            continue;

                        // set triggers to 'executing'
                        List<TriggerFiredResult> bndles = new ArrayList<TriggerFiredResult>();

                        boolean goAhead = true;
                        synchronized(sigLock) {
                            goAhead = !halted.get();
                        }
                        if(goAhead) {
                            try {
                                //**********triggersFired方法主要有几个作用:
                                //**********(1)取出触发器对应应执行的任务
                                //**********(2)记录触发器的执行，修改触发器的状态，如果对应的任务是StatefulJob，则阻塞其他触发器
                                //**********(3)调整触发器下次执行的时间
                                List<TriggerFiredResult> res = qsRsrcs.getJobStore().triggersFired(triggers);
                                if(res != null)
                                    bndles = res;
                            } catch (SchedulerException se) {
                                qs.notifySchedulerListenersError(
                                        "An error occurred while firing triggers '"
                                                + triggers + "'", se);
                                //QTZ-179 : a problem occurred interacting with the triggers from the db
                                //we release them and loop again
                                for (int i = 0; i < triggers.size(); i++) {
                                    qsRsrcs.getJobStore().releaseAcquiredTrigger(triggers.get(i));
                                }
                                continue;
                            }

                        }

                        for (int i = 0; i < bndles.size(); i++) {
                            //**********这个循环就是将当前取出的触发器挨个执行，并触发相应的监听器
                            TriggerFiredResult result =  bndles.get(i);
                            TriggerFiredBundle bndle =  result.getTriggerFiredBundle();
                            Exception exception = result.getException();

                            if (exception instanceof RuntimeException) {
                                getLog().error("RuntimeException while firing trigger " + triggers.get(i), exception);
                                qsRsrcs.getJobStore().releaseAcquiredTrigger(triggers.get(i));
                                continue;
                            }

                            // it's possible to get 'null' if the triggers was paused,
                            // blocked, or other similar occurrences that prevent it being
                            // fired at this time...  or if the scheduler was shutdown (halted)
                            if (bndle == null) {
                                qsRsrcs.getJobStore().releaseAcquiredTrigger(triggers.get(i));
                                continue;
                            }

                            JobRunShell shell = null;
                            try {
                                shell = qsRsrcs.getJobRunShellFactory().createJobRunShell(bndle);
                                shell.initialize(qs);
                            } catch (SchedulerException se) {
                                qsRsrcs.getJobStore().triggeredJobComplete(triggers.get(i), bndle.getJobDetail(), CompletedExecutionInstruction.SET_ALL_JOB_TRIGGERS_ERROR);
                                continue;
                            }
                            //**********从线程池中取出线程执行任务
                            if (qsRsrcs.getThreadPool().runInThread(shell) == false) {
                                // this case should never happen, as it is indicative of the
                                // scheduler being shutdown or a bug in the thread pool or
                                // a thread pool being used concurrently - which the docs
                                // say not to do...
                                getLog().error("ThreadPool.runInThread() return false!");
                                qsRsrcs.getJobStore().triggeredJobComplete(triggers.get(i), bndle.getJobDetail(), CompletedExecutionInstruction.SET_ALL_JOB_TRIGGERS_ERROR);
                            }

                        }
                        //**********执行完后重新再取下一批触发器
                        continue; // while (!halted)
                    }
                } else { // if(availThreadCount > 0)
                    // should never happen, if threadPool.blockForAvailableThreads() follows contract
                    continue; // while (!halted)
                }
                //**********若本次循环未取出触发器，则阻塞一段时间(随机时间)，然后再重试
                long now = System.currentTimeMillis();
                long waitTime = now + getRandomizedIdleWaitTime();
                long timeUntilContinue = waitTime - now;
                synchronized(sigLock) {
                    try {
                        if(!halted.get()) {
                            // QTZ-336 A job might have been completed in the mean time and we might have
                            // missed the scheduled changed signal by not waiting for the notify() yet
                            // Check that before waiting for too long in case this very job needs to be
                            // scheduled very soon
                            if (!isScheduleChanged()) {
                                sigLock.wait(timeUntilContinue);
                            }
                        }
                    } catch (InterruptedException ignore) {
                    }
                }

            } catch(RuntimeException re) {
                getLog().error("Runtime error occurred in main trigger firing loop.", re);
            }
        } // while (!halted)

        // drop references to scheduler stuff to aid garbage collection...
        qs = null;
        qsRsrcs = null;
    }

   **/
}
