package com.sumu.jobclient.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-04 09:38
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Component
public @interface JobHandler {
    String value();
}
