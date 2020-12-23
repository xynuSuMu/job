package com.sumu.jobserver.api;

import com.sumu.common.core.Result;
import com.sumu.jobserver.api.service.JobService;
import com.sumu.jobserver.api.vo.JobInstanceVO;
import com.sumu.jobserver.api.vo.param.AddJobVO;
import com.sumu.jobserver.api.vo.JobDefinitionVO;
import com.sumu.jobserver.api.vo.query.JobDefinitionQuery;
import com.sumu.jobserver.api.vo.query.JobInstanceQuery;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 15:54
 */
@RestController
@RequestMapping("job/")
public class JobApi {


    @Autowired
    private JobService jobService;

    @PostMapping("add")
    public Result<String> addJob(@RequestBody AddJobVO addJobVO) {
        try {
            jobService.addJob(addJobVO);
            return Result.success("Success");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return Result.fail("Fail");
    }

    @PostMapping("definition/list")
    public Result<List<JobDefinitionVO>> jobDefinitionList(@RequestBody JobDefinitionQuery jobDefinitionQuery) {
        return Result.success(jobService.jobDefinitionList(jobDefinitionQuery));
    }

    @PostMapping("instance/list")
    public Result<List<JobInstanceVO>> jobInstanceList(@RequestBody JobInstanceQuery jobInstanceQuery) {
        return Result.success(jobService.jobInstanceList(jobInstanceQuery));
    }
}
