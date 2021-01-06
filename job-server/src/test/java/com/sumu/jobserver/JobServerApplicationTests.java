package com.sumu.jobserver;

import com.alibaba.fastjson.JSONObject;
import com.sumu.jobserver.api.AppApi;
import com.sumu.jobserver.api.JobApi;
import com.sumu.jobserver.api.vo.JobDefinitionVO;
import com.sumu.jobserver.api.vo.JobInstanceVO;
import com.sumu.jobserver.api.vo.param.AddJobVO;
import com.sumu.jobserver.api.vo.param.JavaJobVO;
import com.sumu.jobserver.api.vo.query.JobDefinitionQuery;
import com.sumu.jobserver.api.vo.query.JobInstanceQuery;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class JobServerApplicationTests {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AppApi appApi;

    @Autowired
    private JobApi jobApi;

    @Test
    void appTest() {
        LOG.info("[ TEST ] app list ,{}", JSONObject.toJSONString(appApi.getAppList()));
        //data:{"appName":"job","id":1}
    }

    @Test
    void getDag() {
        System.out.println(JSONObject.toJSONString(jobApi.getJobDefinitionDAG(4).getData()));
    }

    @Test
    void addJobTest() {

        int appId = 1;
        String jobName = "测试Job的名称9";
        String jobDesc = "测试Job的描述";
        String handlerName = "demoTask";
        //每一分钟执行一次
        String cron = "0 0/2 * * * ?";
        Boolean enable = true;
        //策略，1-默认 2-集群 3-分片
        int strategy = 1;
        //任务类型
        int type = 1;
        JavaJobVO javaJobVO = new JavaJobVO();
        javaJobVO.setHandlerName(handlerName);
        javaJobVO.setStrategy(strategy);

        AddJobVO addJobVO = new AddJobVO();
        addJobVO.setAppId(appId);
        addJobVO.setJobName(jobName);
        addJobVO.setJobDesc(jobDesc);
        addJobVO.setCron(cron);
        addJobVO.setEnable(enable);
        addJobVO.setTaskType(type);
        addJobVO.setJavaJobVO(javaJobVO);

        jobApi.addJob(addJobVO);

    }

    @Test
    void jobDefinitionList() {
        JobDefinitionQuery jobDefinitionQuery = new JobDefinitionQuery();
        jobDefinitionQuery.setPageIndex(1);
        jobDefinitionQuery.setPageSize(10);

        List<JobDefinitionVO> jobDefinitionVOS = jobApi.jobDefinitionList(jobDefinitionQuery).getData();
        LOG.info("result:{}", JSONObject.toJSONString(jobDefinitionVOS));
    }


    @Test
    void jobInstanceList() {
        JobInstanceQuery jobInstanceQuery = new JobInstanceQuery();
        jobInstanceQuery.setJonDefinitionID(1);
        jobInstanceQuery.setPageIndex(1);
        jobInstanceQuery.setPageSize(10);

        List<JobInstanceVO> jobDefinitionVOS = jobApi.jobInstanceList(jobInstanceQuery).getData();
        LOG.info("result:{}", JSONObject.toJSONString(jobDefinitionVOS));
    }


}
