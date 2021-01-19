package com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.shell;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-19 14:36
 */
public interface ShellJobDefinitionEntity extends ShellJobDefinition {
    void setId(int id);

    void setDefinitionID(int definitionID);

    void setUser(String user);

    void setHost(String host);

    void setPort(int port);

    void setPassword(String password);

    void setDirectory(String directory);

    void setFile(String file);

    void setParam(String param);
}
