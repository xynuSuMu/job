package com.sumu.jobclient;

import com.sumu.jobclient.annotation.JobHandler;
import com.sumu.jobclient.common.Context;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Map;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-04 10:05
 */
public class JobManager implements ApplicationContextAware {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());


    public JobManager() {

    }


    public void start() throws Exception {
        ConfigurableEnvironment environment = (ConfigurableEnvironment) Context.getApplicationContext().getEnvironment();
        String zkAddress = environment.getProperty("job.zkAddress");
        CuratorFramework client = CuratorFrameworkFactory.newClient(zkAddress, new ExponentialBackoffRetry(1000, 3));
        client.start();
        if (client.checkExists().forPath("/job/workers") == null) {
            LOG.error("[zk for path /job is not exist]");
        } else {
            String info = "Test Job Info";
            String appName = environment.getProperty("app.name");
            String ph = getChildrenNode(appName);
            if (client.checkExists().forPath(ph) != null)
                client.delete().forPath(ph);
            client.create().withMode(CreateMode.EPHEMERAL)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath(getChildrenNode(appName), info.getBytes());
        }
        LOG.info("-----");
    }

    public void destroy() {
        System.out.println("======");
    }

    public String getChildrenNode(String appName) {
        String name = appName;
        String ip = "127.0.0.1";
        String port = "8080";

        return "/job/workers/" + name + "|" + ip + ":" + port;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Context.setApplicationContext(applicationContext);
        Map<String, Object> serviceBeanMap = applicationContext.getBeansWithAnnotation(JobHandler.class);
        System.out.println(serviceBeanMap.toString());
    }
}
