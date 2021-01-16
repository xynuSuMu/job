package com.sumu.jobserver.api.service;

import com.alibaba.fastjson.JSONObject;
import com.sumu.jobserver.api.vo.AppVO;
import com.sumu.jobscheduler.scheduler.core.service.JobApplicationService;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.app.App;
import com.sumu.jobscheduler.scheduler.mapper.AppMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-22 19:12
 */
@Service
public class AppService {

    @Autowired
    private JobApplicationService jobApplicationService;

    public List<AppVO> getAppList() {
        List<AppVO> res = new ArrayList<>();
        List<App> list = jobApplicationService
                .createAppQuery().list();
        list.stream().forEach(appDO -> {
            AppVO appVO = new AppVO();
            appVO.setAppName(appDO.getAppCode());
            appVO.setId(appDO.getID());
            res.add(appVO);
        });

        return res;
    }

    @Transactional
    public void insertTransactionTest() {
        jobApplicationService.createAppBuilder()
                .appCode("1234")
                .zxID(99)
                .updateTime(new Date())
                .create();
        throw new RuntimeException("123");
    }


}

