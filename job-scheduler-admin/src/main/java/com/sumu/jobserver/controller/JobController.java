package com.sumu.jobserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Auther: chenlong
 * @Date: 2021/1/2 18:59
 * @Description:后台界面
 */
@Controller
@RequestMapping("manager/")
public class JobController {


    @RequestMapping("jobList.html")
    public String jobList() {
        return "job.html";
    }

    @RequestMapping("job-instance.html")
    public String jobInstanceList() {
        return "job-instance.html";
    }

    @RequestMapping("dag.html")
    public String jobDag() {
        return "dag.html";
    }
}
