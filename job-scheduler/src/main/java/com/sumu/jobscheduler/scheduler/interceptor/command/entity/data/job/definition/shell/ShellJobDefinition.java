package com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.shell;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-19 14:35
 */
public interface ShellJobDefinition {

    String COMMAND = "sh";

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

    default String exec() {
        StringBuilder stringBuilder = new StringBuilder();
        Session session = null;
        ChannelExec channel = null;
        try {

            InputStream input;
            session = getSession();
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(COMMAND + " " + getDirectory() + getFile() + " " + getParam());
            input = channel.getInputStream();
            channel.connect(10000);
            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(input));
                String inputLine;
                while ((inputLine = inputReader.readLine()) != null) {
                    stringBuilder.append(inputLine + "\n");
                }
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (JSchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != channel) {
                channel.disconnect();
            }
            if (null != session) {
                session.disconnect();
            }
        }
        return stringBuilder.toString();
    }
}
