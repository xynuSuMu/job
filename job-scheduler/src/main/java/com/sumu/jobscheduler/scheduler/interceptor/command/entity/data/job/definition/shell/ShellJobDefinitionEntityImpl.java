package com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.shell;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-19 14:32
 */
public class ShellJobDefinitionEntityImpl implements ShellJobDefinitionEntity {
    private int id;
    private int definitionID;
    private String user;
    private String host;
    private int port;
    private String password;
    private String directory;
    private String file;
    private String param;

    public int getId() {
        return id;
    }

    public int getDefinitionID() {
        return definitionID;
    }

    public String getUser() {
        return user;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPassword() {
        return password;
    }

    public String getDirectory() {
        return directory;
    }

    public String getFile() {
        return file;
    }

    public String getParam() {
        return param;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDefinitionID(int definitionID) {
        this.definitionID = definitionID;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Session getSession() throws JSchException {
        JSch jSch = new JSch();
        Session session = jSch.getSession(user, host, port);
        session.setPassword(password);
        session.setConfig("PreferredAuthentications", "password");
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect(5000);
        return session;
    }


}
