package com.sumu.jobserver.api;

import com.sumu.common.core.Result;
import com.sumu.jobserver.api.service.AppService;
import com.sumu.jobserver.api.vo.AppVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-22 19:07
 */
@RestController()
@RequestMapping("app/")
public class AppApi {

    @Autowired
    private AppService appService;

    @RequestMapping("getAppList")
    @ResponseBody
    public Result<List<AppVO>> getAppList() {

        return Result.success(appService.getAppList());
    }
}
