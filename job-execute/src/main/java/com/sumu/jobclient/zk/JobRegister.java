package com.sumu.jobclient.zk;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumu.jobclient.common.Context;
import com.sumu.jobclient.modal.zk.ZkDataModal;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.sumu.common.core.ZKConstants.JOB_REGISTER;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-04 15:14
 */
public class JobRegister {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final String JOB_WORKS_DIR = JOB_REGISTER;

    public void register() throws Exception {
        String zkAddress = Context.getJobProperties().getZkAddress();
        CuratorFramework client = CuratorFrameworkFactory
                .newClient(zkAddress, new ExponentialBackoffRetry(1000, 3));
        client.start();
        if (client.checkExists().forPath(JOB_WORKS_DIR) == null) {
            LOG.error("[zk for path /job/workers is not exist]");
            client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(JOB_WORKS_DIR);
        }
        //组册信息
        ZkDataModal zkDataModal = new ZkDataModal();
        zkDataModal.setIp(Context.getIP());
        zkDataModal.setPort(Context.getPORT());
        zkDataModal.setAppName(Context.getAppProperties().getName());
        zkDataModal.setHostName(Context.getHOSTNAME());
        byte[] info = new ObjectMapper().writeValueAsBytes(zkDataModal);
        String ph = getChildrenNode();
        if (client.checkExists().forPath(ph) != null)
            client.delete().forPath(ph);
        client.create().withMode(CreateMode.EPHEMERAL)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath(getChildrenNode(), info);
    }

    private String getChildrenNode() {
        String name = Context.getAppProperties().getName();
        String ip = Context.getIP();
        int port = Context.getPORT();
        return JOB_WORKS_DIR + "/" + name + "|" + ip + ":" + port;
    }
}
