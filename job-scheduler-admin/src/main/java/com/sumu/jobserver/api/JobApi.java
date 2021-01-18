package com.sumu.jobserver.api;

import com.sumu.common.core.Result;
import com.sumu.jobserver.api.service.JobService;
import com.sumu.jobserver.api.vo.JobInstanceVO;
import com.sumu.jobserver.api.vo.Page;
import com.sumu.jobserver.api.vo.dag.DagVO;
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
 * @Description Job任务信息
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
            return Result.success("addJob Success");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("addJob Fail:" + e.getMessage());
        }
    }

    @PostMapping("edit")
    public Result<String> editJob(@RequestBody AddJobVO addJobVO) {
        try {
            jobService.editJob(addJobVO);
            return Result.success("editJob Success");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("editJob Fail:" + e.getMessage());
        }
    }

    @PutMapping("pause/{id}")
    public Result<String> pause(@PathVariable int id) {
        try {
            jobService.pause(id);
            return Result.success("Pause Success");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("Pause Fail:" + e.getMessage());
        }
    }

    @PutMapping("delete/{id}")
    public Result<Boolean> delete(@PathVariable int id) {
        try {
            jobService.delete(id);
            return Result.success(true);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return Result.success(false);
    }

    @PutMapping("resume/{id}")
    public Result<String> resume(@PathVariable int id) {
        try {
            jobService.resume(id);
            return Result.success("Resume Success");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("Resume Fail:" + e.getMessage());
        }
    }

    @PutMapping("trigger/{id}")
    public Result<String> trigger(@PathVariable int id) {
        try {
            jobService.trigger(id);
            return Result.success("Trigger Success");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("Trigger Fail:" + e.getMessage());
        }
    }


    @PostMapping(value = "definition/list")
    @ResponseBody
    public Result<Page<List<JobDefinitionVO>>> jobDefinitionList(@RequestBody JobDefinitionQuery jobDefinitionQuery) {
        return Result.success(jobService.jobDefinitionList(jobDefinitionQuery));
    }


    @GetMapping(value = "definition/detail/{id}")
    @ResponseBody
    public Result<JobDefinitionVO> jobDefinitionDetail(@PathVariable int id) {
        return Result.success(jobService.jobDefinitionDetail(id));
    }

    @PostMapping("instance/list")
    public Result<Page<List<JobInstanceVO>>> jobInstanceList(@RequestBody JobInstanceQuery jobInstanceQuery) {
        return Result.success(jobService.jobInstanceList(jobInstanceQuery));
    }

    @GetMapping("definition/dag/{id}")
    @ResponseBody
    public Result<DagVO> getJobDefinitionDAG(@PathVariable int id) {
        return Result.success(jobService.getJobDefinitionDAG(id));
    }
}
