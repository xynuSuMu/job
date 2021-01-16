package com.sumu.jobserver;

import com.alibaba.fastjson.JSONObject;
import com.sumu.jobserver.api.service.AppService;
import com.sumu.jobscheduler.scheduler.core.service.JobDefinitionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-12 11:25
 */

@SpringBootTest
public class CommandTests {

    @Autowired
    JobDefinitionService jobDefinitionService;

    @Test
    void countJobDefinition() {
        System.out.println(JSONObject.toJSONString(
                jobDefinitionService.createJavaQuery().definitionId(9).singleResult()
        ));
//        System.out.println(JSONObject.toJSONString(
//                jobDefinitionService.createQuery()
//                        .index(1)
//                        .pageSize(5)
//                        .list()
//        ));
//        System.out.println(
//                jobDefinitionService.createQuery().jobName(
//                        "DAG任务测试-2-V2"
//                ).count()
//        );
    }

    @Autowired
    AppService appService;

    @Test
    void rollBack() {
        appService.insertTransactionTest();
    }
}
