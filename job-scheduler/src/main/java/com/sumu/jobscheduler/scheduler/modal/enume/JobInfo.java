package com.sumu.jobscheduler.scheduler.modal.enume;

import com.sumu.jobscheduler.scheduler.core.schedule.AbstractJobExecutor;
import com.sumu.jobscheduler.scheduler.core.schedule.java.JobExecutor;
import com.sumu.jobscheduler.scheduler.core.schedule.shell.ShellExecutor;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-24 12:47
 */
public interface JobInfo {

    enum Type {
        JAVA(1, "Java", JobExecutor.class),
        SHELL(2, "SHELL脚本", ShellExecutor.class),
        ;
        private int code;
        private String desc;
        private Class<?> clazz;

        Type(int code, String desc, Class<?> clazz) {
            this.code = code;
            this.desc = desc;
            this.clazz = clazz;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public Class<?> getClazz() {
            return clazz;
        }

        public static Class<? extends  AbstractJobExecutor> getClazzByCode(int code) {
            for (Type type : values()) {
                if (type.code == code) {
                    return (Class<? extends AbstractJobExecutor>) type.clazz;
                }
            }
            return null;
        }
    }

}
