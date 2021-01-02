package com.sumu.jobserver.api;

import com.sumu.common.core.Result;
import com.sumu.jobserver.api.service.AppService;
import com.sumu.jobserver.api.vo.AppVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-22 19:07
 */
@RestController()
@RequestMapping("app/")
@CrossOrigin
public class AppApi {

    @Autowired
    private AppService appService;

    @GetMapping(value = "getAppList")
    @ResponseBody
    public Result<List<AppVO>> getAppList() {

        return Result.success(appService.getAppList());
    }
}
