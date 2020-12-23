package com.sumu.jobclient.rpc;

import com.alibaba.fastjson.JSONObject;
import com.sumu.jobclient.dispatcher.RequestDispatcher;
import com.sumu.common.util.rpc.RpcResult;
import com.sumu.jobclient.modal.job.JobData;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-17 16:11
 */
public class JettyServerHandler extends AbstractHandler {

    private Logger LOG = LoggerFactory.getLogger(JettyServerHandler.class);

    private RequestDispatcher requestDispatcher;

    public JettyServerHandler(JobData jobData) {
        requestDispatcher = new RequestDispatcher(jobData);
    }

    @Override
    public void handle(String s, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOG.info("[Jetty] Request Arrive ,{}", request.getRequestURI());

        RpcResult result = requestDispatcher.dispatcher(request);

        //todo:JSON -> jackson
        byte[] bytes = JSONObject.toJSONString(result).getBytes();

        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        OutputStream out = response.getOutputStream();
        out.write(bytes);
        out.flush();
    }
}
