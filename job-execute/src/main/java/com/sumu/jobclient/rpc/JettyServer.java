package com.sumu.jobclient.rpc;

import com.sumu.jobclient.common.Context;
import com.sumu.jobclient.modal.job.JobData;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-17 16:10
 */
public class JettyServer {

    private Logger LOG = LoggerFactory.getLogger(JettyServer.class);

    private volatile JettyServerHandler handler;

    private JobData jobData;

    private Server server;

    private Thread thread;

    public JettyServer(JobData jobData) {
        this.jobData = jobData;
    }

    public void start() {
        thread = new Thread(() -> {
            //Jetty Server
            server = new Server(new ExecutorThreadPool());

            // HTTP connector
            ServerConnector connector = new ServerConnector(server);

            connector.setHost(Context.getIP());
            connector.setPort(Context.getPORT());
            server.setConnectors(new Connector[]{connector});

            HandlerCollection hc = new HandlerCollection();
            handler = new JettyServerHandler(jobData);
            hc.setHandlers(new Handler[]{handler});
            server.setHandler(hc);
            // Start server
            try {
                server.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            LOG.info("[Jetty] jetty server start successfully at  IP:{},port:{}.", Context.getIP(), Context.getPORT());
            try {
                server.join();
                LOG.info("[Jetty] jetty server join successfully");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    public void destroy() {
        // destroy server
        if (server != null) {
            try {
                server.stop();
                server.destroy();
            } catch (Throwable e) {
                LOG.error(e.getMessage(), e);
            }
        }
        if (thread.isAlive()) {
            thread.interrupt();
        }
        if (handler != null) {
            handler.destroy();
        }
        LOG.info("[jetty] jetty server destroy success");
    }
}
