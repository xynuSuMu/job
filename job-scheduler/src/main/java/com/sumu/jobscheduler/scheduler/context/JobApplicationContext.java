package com.sumu.jobscheduler.scheduler.context;

import com.sumu.jobscheduler.scheduler.interceptor.command.context.CommandContext;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Stack;

/**
 * @author 陈龙
 * @version 1.0r
 * @date 2020-12-22 19:08
 */
public class JobApplicationContext {

    private static CuratorFramework client;

    private static ThreadLocal<Stack<CommandContext>> commandContextThreadLocal = new ThreadLocal();

    private static ThreadLocal<Boolean> special = new ThreadLocal<>();

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

    public static CommandContext getCommandContext() {
        Stack<CommandContext> stack = getStack();
        return stack.isEmpty() ? null : stack.peek();
    }

    public static void setCommandContext(CommandContext commandContext) {
        getStack().push(commandContext);
    }

    public static void removeCommandContext() {
        getStack().pop();
    }

    private static Stack<CommandContext> getStack() {
        Stack<CommandContext> stack = commandContextThreadLocal.get();
        if (stack == null) {
            stack = new Stack<>();
            commandContextThreadLocal.set(stack);
        }
        return stack;
    }

    public static Boolean getSpecial() {
        return special.get();
    }

    public static void setSpecial(Boolean val) {
        special.set(val);
    }
}
