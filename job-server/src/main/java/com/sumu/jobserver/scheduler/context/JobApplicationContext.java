package com.sumu.jobserver.scheduler.context;

import org.apache.curator.framework.CuratorFramework;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-22 19:08
 */
public class JobApplicationContext {

    private static CuratorFramework client;

    private static final String IP;

    static {
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        IP = addr.getHostAddress();

    }

    public static String getIP() {
        return IP;
    }

    public static CuratorFramework getClient() {

        return client;
    }

    public static void setClient(CuratorFramework client) {
        JobApplicationContext.client = client;
    }
}
