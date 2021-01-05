package com.sumu.jobserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.sumu.jobserver.mapper")
public class JobServerApplication {

    public static void main(String[] args) {
        //app.addListeners(new BeanDefinitionPostProcess());
        SpringApplication.run(JobServerApplication.class, args);
    }

}
