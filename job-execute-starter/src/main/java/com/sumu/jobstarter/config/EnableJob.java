package com.sumu.jobstarter.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-04 09:07
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({JobConfiguration.class})
public @interface EnableJob {
}
