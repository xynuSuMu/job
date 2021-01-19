package com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.shell;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-19 09:41
 */
public class Remote {

    private String user = "chenlong";
    private String host = "127.0.0.1";
    private int port = 22;
    private String password = "250724";
//    private String identity = "~/.ssh/id_rsa";
//    private String passphrase = "";


    public Session getSession() throws JSchException {
        JSch jSch = new JSch();
//        if (Files.exists(Paths.get(identity))) {
//            jSch.addIdentity(identity, passphrase);
//        }
        Session session = jSch.getSession(user, host, port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.setConfig("PreferredAuthentications", "password");
        session.connect(5000);
        return session;
    }


}
