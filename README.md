# job - 分布式任务调度系统

* Java8
* SpringBoot
* Zookeeper
* Quartz
* Jetty
* Feign
* Jackson
* ThreadPoolExecutor

### Demo

等待更新......

### 线程池管理工具

在分布式任务调度系统中，客户端任务在特定场景下使用多线程进行任务处理，如数据同步。

问题一：多线程如果同步时间过久(死循环或无超时的Http请求)，造成后续任务无法正常执行。

问题二：任务调度系统成功执行，但是线程内部执行异常，无法监控。

基于上面问题，对于JDK原生ThreadPoolExecutor进行改造，改造类为ThreadPoolExecutorManager，
该类的核心目标为：任务堆积时告警、拒绝任务时告警、核心参数修改、线程执行情况监听、线程中断(对于IO阻塞，无法中断)。

![image](image/ThreadPoolManager.png)

### 分布式任务调度

Client模块定义JobHandler注解和抽象类AbstractJobHandler，IOC容器启动后会初始化 JobManager，
JobManager负责：注册应用信息、获取JobHandler、启动Jetty。

Starter模块负责定义配置信息(spring-configuration-metadata.json)以及import类(JobConfiguration)的实现

Server模块监听注册中心的应用信息，提供Job定义(JobDefinition)的可视化界面，Job定义后会写入调度中心(Scheduler)，
调度中心负责每次Job的执行(JobInstance)

#### Client

![image](http://www.study-java.cn/v2/image/1608624227635JobClient.png)


### Server

![image](http://www.study-java.cn/v2/image/1608624678483JobServer.png)