package com.sumu.demo;

import com.sumu.demo.threadpool.SimpleThreadPool;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void testThreadPool() {
        SimpleThreadPool simpleThreadPool = new SimpleThreadPool();
        simpleThreadPool.test();
    }

}
