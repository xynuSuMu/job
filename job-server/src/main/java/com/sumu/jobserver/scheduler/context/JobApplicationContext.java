package com.sumu.jobserver.scheduler.context;

import com.sumu.jobserver.scheduler.interceptor.command.context.CommandContext;
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

    private static ThreadLocal<CommandContext> commandContextThreadLocal = new ThreadLocal<>();

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

    public static CommandContext getCommandContext() {
        return commandContextThreadLocal.get();
    }

    public static void setCommandContext(CommandContext commandContext) {
        commandContextThreadLocal.set(commandContext);
    }

    public static void removeCommandContext(CommandContext commandContext) {
        if (commandContext.openSqlSession())
            commandContext.closeSqlSession();
        commandContextThreadLocal.remove();
    }

    public static void setClient(CuratorFramework client) {
        JobApplicationContext.client = client;
    }
}
