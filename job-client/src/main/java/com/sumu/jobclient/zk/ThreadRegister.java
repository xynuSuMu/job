package com.sumu.jobclient.zk;

import com.alibaba.fastjson.JSONObject;
import com.sumu.jobclient.modal.threadpool.ThreadRegisterModal;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-15 16:15
 */
public class ThreadRegister {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final String PATH = "/job/thread/pool";

    public void register(ThreadRegisterModal threadRegisterModal) throws Exception {

        CuratorFramework client = CuratorFrameworkFactory.newClient(threadRegisterModal.getZkAddress(),
                new ExponentialBackoffRetry(1000, 3));
        client.start();
        if (client.checkExists().forPath(PATH) == null) {
            //todo
            LOG.info("[ " + PATH + " Not Exist]");
        } else {
            String ph = getChildrenNode(threadRegisterModal);
            if (client.checkExists().forPath(ph) != null)
                client.delete().forPath(ph);
            client.create().withMode(CreateMode.EPHEMERAL)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath(getChildrenNode(threadRegisterModal),
                            JSONObject.toJSONBytes(threadRegisterModal));
        }
    }


    private String getChildrenNode(ThreadRegisterModal threadRegisterModal) throws UnknownHostException {

        return PATH + "/" + threadRegisterModal.getAppName()
                + "|" + threadRegisterModal.getIp()
                + ":" + threadRegisterModal.getPort();
    }
}
