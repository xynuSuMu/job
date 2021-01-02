package com.sumu.jobserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: chenlong
 * @Date: 2021/1/2 18:59
 * @Description:
 */
@Controller
@RequestMapping("manager/")
public class JobController {

    @RequestMapping("jobList")
    public String jobList(HttpServletRequest request,
                        @RequestParam(value = "name", required = false, defaultValue = "springboot-thymeleaf") String name) {
        request.setAttribute("name", name);
        return "job.html";
    }
}
