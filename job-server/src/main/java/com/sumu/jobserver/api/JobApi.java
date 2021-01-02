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
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 15:54
 */
@RestController
@RequestMapping("job/")
@CrossOrigin
public class JobApi {


    @Autowired
    private JobService jobService;

    @PostMapping("add")
    public Result<String> addJob(@RequestBody AddJobVO addJobVO) {
        try {
            jobService.addJob(addJobVO);
            return Result.success("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("Fail:" + e.getMessage());
        }
    }

    @PutMapping("pause/{id}")
    public Result<String> pause(@PathVariable int id) {
        try {
            jobService.pause(id);
            return Result.success("Pause Success");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("Fail:" + e.getMessage());
        }
    }

    @PutMapping("resume/{id}")
    public Result<String> resume(@PathVariable int id) {
        try {
            jobService.resume(id);
            return Result.success("resume Success");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("Resume Fail:" + e.getMessage());
        }
    }


    @PostMapping(value = "definition/list")
    @ResponseBody
    public Result<List<JobDefinitionVO>> jobDefinitionList(@RequestBody JobDefinitionQuery jobDefinitionQuery) {
        return Result.success(jobService.jobDefinitionList(jobDefinitionQuery));
    }

    @PostMapping("instance/list")
    public Result<List<JobInstanceVO>> jobInstanceList(@RequestBody JobInstanceQuery jobInstanceQuery) {
        return Result.success(jobService.jobInstanceList(jobInstanceQuery));
    }
}
