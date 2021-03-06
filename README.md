# 分布式任务调度系统 - JOB

### 文档

待补充.....

### 背景

最为出名的XXL-JOB使用自研调度组件，elastic-job基于Quartz在上层基于ZK做了一层主从机器，由主结点进行作业分片，
从结点具体任务调度。个人之前基于Quartz实现的分布式任务调度系统并未解决Quartz在弹性扩容方面的不足，借鉴于elastic-job
的实现进行新的设计：调度中心每一台机器都参与Trigger的扫描，根据调度中心的机器总数和当前机器的编号进行取模调度、支持核心业务
的任务由单独机器部署

### 目标

* 提供可视化后台操作界面，包含JOB定义列表、JOB执行实例列表、开启/暂停任务等
* 提供多种任务调度类型：Java、Shell、Hadoop(未开始)、Hive(未开始)、Python(未开始)
* 调度中心HA，Quartz本身支持集群部署、改造优化DB锁竞争
* 执行器HA，基于ZK注册，支持执行器集群部署
* 核心业务独立调度：改造Quartz表，特殊任务或核心任务可直接指定部分机器单独调度。
* 支持依赖任务以及DAG展示
* 支持多种调度策略：集群:随机、轮寻、Hash、广播、分片
* 异常处理：故障转移、重试、预警、
* 调度器-执行器的通讯验证(未开始)
* 执行器提供自定义线程池
* 弹性扩容：机器上线/下线，下次调度时将会重新分配任务
* 权限控制(未开始)

### 后台界面

* Job列表

![image](image/JobListHtml.png)

* Job实例列表

![image](image/JobDetailHtml.jpg)

* DAG

![image](image/DAG.png)

### 部署

* 安装JDK、Zookeeper、MySQL环境
* 启动Zookeeper、MySQL
* 初始化SQL，执行admin模块resource目录下init.sql
* 修改demo模块、admin模块application.properties文件
```properties
#当前应用名称
app.name = job
#zk地址
job.zkAddress=127.0.0.1
```
* demo模块编写自定义JobHandler
```java
@JobHandler("demoTask")
public class Task extends AbstractJobHandler {
        
    /**
    * JobParam中包含Job实例ID，分片索引，分片数量
    */
    @Override
    public <T> Result<T> execute(JobParam jobParam) throws Exception {
           
     }
}
```
* 启动demo模块
* 启动admin模块
* 访问admin后台页面，配置Job任务。默认端口8089，http://127.0.0.1:8089/manager/jobList.html

技术栈：

* Java8
* SpringBoot
* Zookeeper
* Quartz
* Jetty
* Feign
* Jackson
* ThreadPoolExecutor

### 关于Quartz

Quartz是任务调度领域的一个开源项目，它提供了很多实用的特性，比如持久化、集群和分布式任务、多线程管理等，像elastic-job以及
早期的XXL-Job都是基于Quartz实现的分布式任务调度系统。

Quartz的工作原理：org.quartz.core的目录下的QuartzScheduler类是Quartz的启动入口，其构造函数内部会创建QuartzSchedulerThread
对象，该对象继承Thread类作为Quartz的调度线程，调度线程线程池(SimpleThreadPool)中获取可用的线程，然后从JobStore(ARM/DB)中的获取
30S(默认30S，为避免频繁扫表，可适当增加该时间)内即将执行的触发器(即将执行的触发器是由另一线程进行插入)，然后通过线程池的线程进行调度，
由JobRunShell负责执行。

在集成Quartz时，通常我们可以利用Spring对于Quartz的包装来实现，比如SpringBoot项目中存在的QuartzAutoConfiguration类，该类在
spring.factories中被声明为EnableAutoConfiguration，所以SpringBoot项目启动后会创建SchedulerFactoryBean，该Bean实现了InitializingBean
所以在初始化Bean的时候，会执行afterPropertiesSet方法，也正是在该方法内部调用了SchedulerFactory的getScheduler方法，进而启动
Quartz。

在基于Spring和Quartz实现的分布式任务调度系统，不可避免会因为出Quartz的不足出现一些问题，如默认配置下ABA问题，机器负载不均衡的问题。

#### ABA问题
乐观锁ABA问题：
Quartz默认使用乐观锁形式进行获取Trigger，乐观锁就会存在ABA的问题，在JobStoreSupport中：

```java
this.getDelegate().updateTriggerStateFromOtherState(conn, triggerKey, "ACQUIRED", "WAITING");
```

将 triggerKey 对应的数据(QRTZ_TRIGGERS表)TRIGGER_STATE由WAITINGA变更为ACQUIRED，低概率下会出现如下问题：
由于波动，或CPU资源被抢占，那么可能会进入停顿，此时另一机器完成另整个过程WAITING->ACQUIRED->EXECUTING->WAITING，
那么该Job就会出现多次触发。

#### 负载不均衡

Quartz支持集群方式，但是任务调度采用抢占式，集群机器数量越多，就会越多机器参与DB锁的资源竞争，造成资源浪费。

本次调度系统对Spring的SchedulerFactoryBean配置类进行改造，使用自定义的org.quartz.jobStore.class，重写
acquireNextTrigger方法，根据当前机器编号和总的机器数量，利用Trigger的hashCode%机器数量，等于当前机器编号
的Trigger则由当前机器执行，当前机器编号和总的机器数量实时从ZK上获取。此外针对需要保证调度低延迟的任务可单独
提供机器进行调度。


### 模块介绍

Client模块定义JobHandler注解和抽象类AbstractJobHandler，IOC容器启动后会初始化 JobManager，
JobManager负责：注册应用信息、获取JobHandler、启动Jetty。

Starter模块负责定义配置信息(spring-configuration-metadata.json)以及import类(JobConfiguration)的实现

Server模块监听注册中心的应用信息，提供Job定义(JobDefinition)的可视化界面，Job定义后会写入调度中心(Scheduler)，
调度中心负责每次Job的执行(JobInstance)

Common模块保存Client和Server通用信息

#### Client

![image](image/JobClient.png)


### Server

![image](image/JobServer.png)


### 执行器的自定义线程池

在分布式任务调度系统中，客户端任务在特定场景下使用多线程进行任务处理，如数据同步。

问题一：多线程如果同步时间过久(死循环或无超时的Http请求)，造成后续任务无法正常执行。

问题二：任务调度系统成功执行，但是线程内部执行异常，无法监控。

基于上面问题，对于JDK原生ThreadPoolExecutor进行改造，改造类为ThreadPoolExecutorManager，
该类的核心目标为：任务堆积时告警、拒绝任务时告警、核心参数修改、线程执行情况监听、线程中断(对于IO阻塞，无法中断)。

![image](image/ThreadPoolManager.png)

