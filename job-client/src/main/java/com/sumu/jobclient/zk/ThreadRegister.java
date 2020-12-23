package com.sumu.jobclient.zk;

import com.alibaba.fastjson.JSONObject;
import com.sumu.jobclient.common.Context;
import com.sumu.jobclient.modal.threadpool.ThreadRegisterModal;
import com.sumu.jobclient.threadpool.ThreadPoolExecutorManager;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-15 16:15
 */
public class ThreadRegister {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    private final String PATH = "/zk/thread/pool";

    public void register(String className, ThreadPoolExecutorManager threadPoolExecutorManager) {
        if (Context.getApplicationContext() != null) {
            LOG.info("[ ThreadPoolExecutorManager JobRegister ]");
            try {
                ThreadRegisterModal registerModal = getRegisterModal(className);//new ThreadRegisterModal();
                register(registerModal);
                threadPoolExecutorManager.setRegister(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Context.addThreadPoolManger(className, threadPoolExecutorManager);
    }

    public void register() {
        LOG.info("[ ThreadPoolExecutorManager JobRegister ]");
        Map<String, List<ThreadPoolExecutorManager>> map = Context.getThreadManager();

        Set<Map.Entry<String, List<ThreadPoolExecutorManager>>> set = map.entrySet();
        for (Map.Entry<String, List<ThreadPoolExecutorManager>> entry : set) {
            List<ThreadPoolExecutorManager> threadPoolExecutorManagers = entry.getValue();
            for (ThreadPoolExecutorManager threadPoolExecutorManager : threadPoolExecutorManagers) {
                if (!threadPoolExecutorManager.getRegister()) {
                    ThreadRegister threadRegister = new ThreadRegister();
                    try {
                        ThreadRegisterModal registerModal = getRegisterModal(entry.getKey());
                        threadRegister.register(registerModal);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    private void register(ThreadRegisterModal threadRegisterModal) throws Exception {
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

    private ThreadRegisterModal getRegisterModal(String className) throws UnknownHostException {
        ThreadRegisterModal registerModal = new ThreadRegisterModal();
        registerModal.setZkAddress(Context.getJobProperties().getZkAddress());
        registerModal.setAppName(Context.getAppProperties().getName());
        registerModal.setIp(Context.getIP());
        registerModal.setPort(Context.getPORT());
        registerModal.setClassName(className);
        return registerModal;
    }
}
