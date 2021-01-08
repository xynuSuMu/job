package com.sumu.jobserver.util;

import java.io.InputStream;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-08 10:33
 */
public class MyBatisUtils {

    private final static String MYBATIS_CONFIG = "com/sumu/jobserver/db/mapping.xml";

    public static InputStream getInputStream() {
        String path = MYBATIS_CONFIG;
        ClassLoader classLoader = MyBatisUtils.class.getClassLoader();
        InputStream resourceStream = classLoader.getResourceAsStream(path);
        return resourceStream;
    }

}
