package com.sumu.demo;

import com.sumu.demo.threadpool.SimpleThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-18 19:38
 */
@RestController
@RequestMapping("/test")
public class TestControllere {

    //http://127.0.0.1:9090/thread/pool/info?ClassName=com.sumu.demo.threadpool.SimpleThreadPool
    //http://127.0.0.1:8088/test/exe
    @Autowired
    SimpleThreadPool threadPool;

    @RequestMapping("/exe")
    public String test() {
        threadPool.test();
        return "success";
    }

}
