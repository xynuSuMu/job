package com.sumu.jobserver.core.monitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumu.jobserver.mapper.AppMapper;
import com.sumu.jobserver.modal.zk.ZkDataModal;
import com.sumu.jobserver.properties.JobProperties;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

import static com.sumu.common.core.ZKConstants.JOB_REGISTER;
import static com.sumu.common.core.ZKConstants.NODE_REGISTER;
import static org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type.CHILD_ADDED;
import static org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type.CHILD_REMOVED;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 19:21
 */
@Component
public class ZkMonitor {

    @Autowired
    private JobProperties jobProperties;

    @Autowired
    private JobRegisterHandler registerHandler;

    private PathChildrenCache cache;

    private Logger LOG = LoggerFactory.getLogger(ZkMonitor.class);

    private static final int ZK_BASE_SLEEP_TIME_MS = 1000;

    private static final int ZK_MAX_RETRIES = 3;

    private final String DIR = JOB_REGISTER;

    @PostConstruct
    private void initZkListener() throws Exception {
        String zkAddress = jobProperties.getZkAddress();
        LOG.debug("[JOB] zkAddress = " + zkAddress);
        CuratorFramework client = CuratorFrameworkFactory.newClient(zkAddress,
                new ExponentialBackoffRetry(ZK_BASE_SLEEP_TIME_MS, ZK_MAX_RETRIES));
        client.start();
        //todo：临时顺序结点（待测试）
        client.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath(NODE_REGISTER+"/es", "1".getBytes());
        LOG.info("[JOB] rootDir = " + DIR);

        cache = new PathChildrenCache(client, DIR, true);
        cache.start();
        cache.getListenable().addListener((client1, event) -> handleEvent(event));

    }

    void handleEvent(PathChildrenCacheEvent event) {
        try {
            doHandleEvent(event);
        } catch (Throwable t) {
        }
    }

    void doHandleEvent(PathChildrenCacheEvent event) throws IOException {
        if (null == event.getData() || null == event.getData().getData()) {
            LOG.debug("[zk] watch: event.getData() == null, event type is {}", event.getType());
            return;
        }
        if (null == event.getData().getData()) {
            LOG.debug("[zk] watch: event.getData().getData() == null, event type is {}", event.getType());
            return;
        }

        long zxid = event.getData().getStat().getCzxid();
        ZkDataModal data = new ObjectMapper().readValue(event.getData().getData(), ZkDataModal.class);
        if (CHILD_ADDED == event.getType()) {
            LOG.info("[zk] add ZK Node , appCode = {}, hostname = {}, ip = {}, port = {}, zxid = {}", data.getAppName(),
                    data.getHostName(), data.getIp(), data.getPort(), zxid);
            registerHandler.register(data, zxid);
        } else if (CHILD_REMOVED == event.getType()) {
            LOG.info("[zk] remove ZN ode , appCode = {}, ip = {}, port = {}, zxid = {}", data.getAppName(),
                    data.getIp(), data.getPort(), zxid);
            registerHandler.unregister(data, zxid);
        }
    }


}
