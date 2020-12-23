package com.sumu.demo;

import com.sumu.jobclient.rpc.JettyServer;
import com.sumu.jobstarter.config.EnableJob;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
//starter注解
@EnableJob
//client引入
//@Import({com.sumu.jobclient.config.JobManagerConfig.class})
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
