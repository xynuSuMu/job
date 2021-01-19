package com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.shell;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-19 14:35
 */
public interface ShellJobDefinition {
    int getId();

    int getDefinitionID();

    String getUser();

    String getHost();

    int getPort();

    String getPassword();

    String getDirectory();

    String getFile();

    String getParam();

    Session getSession() throws JSchException;
}
