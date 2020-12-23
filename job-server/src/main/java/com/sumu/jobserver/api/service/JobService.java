package com.sumu.jobserver.api.service;

import com.sumu.jobserver.api.vo.JobDefinitionVO;
import com.sumu.jobserver.api.vo.JobInstanceVO;
import com.sumu.jobserver.api.vo.param.AddJobVO;
import com.sumu.jobserver.api.vo.query.JobDefinitionQuery;
import com.sumu.jobserver.api.vo.query.JobInstanceQuery;
import com.sumu.jobserver.core.schedule.JobSchedule;
import com.sumu.jobserver.mapper.AppMapper;
import com.sumu.jobserver.mapper.JobMapper;
import com.sumu.jobserver.modal.app.AppDO;
import com.sumu.jobserver.modal.job.JobDefinitionDO;
import com.sumu.jobserver.modal.job.JobInstanceDO;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-21 15:56
 */
@Service
public class JobService {

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private AppMapper appMapper;

    @Autowired
    private JobSchedule jobSchedule;

    @Transactional(rollbackFor = Exception.class)
    public void addJob(AddJobVO addJobVO) throws SchedulerException {
        //
        AppDO appDO = appMapper.getAppById(addJobVO.getAppId());
        Assert.isTrue(appDO != null, "当前应用不存在");
        String jobName = addJobVO.getJobName();
        int count = jobMapper.countByJobName(jobName);
        Assert.isTrue(count == 0, "当前任务名称已存在");
        JobDefinitionDO jobDefinitionDO = new JobDefinitionDO();
        BeanUtils.copyProperties(addJobVO, jobDefinitionDO);
        jobMapper.insertJobDefinition(jobDefinitionDO);
        //增加到
        jobSchedule.addJob(String.valueOf(jobDefinitionDO.getId()),
                appDO.getAppName(),
                addJobVO.getCron());
    }

    public List<JobDefinitionVO> jobDefinitionList(JobDefinitionQuery jobDefinitionQuery) {
        jobDefinitionQuery.setPageIndex((jobDefinitionQuery.getPageIndex() - 1) * jobDefinitionQuery.getPageSize());

        List<JobDefinitionDO> list = jobMapper.jobDefinitionList(jobDefinitionQuery);

        Map<Integer, AppDO> map = appMapper.getApps().stream().collect(Collectors.toMap(AppDO::getId, Function.identity()));
        List<JobDefinitionVO> res = new ArrayList<>();
        list.stream().forEach(jobDefinitionDO -> {
            JobDefinitionVO jobDefinitionVO = new JobDefinitionVO();
            jobDefinitionVO.setAppName(map.get(jobDefinitionDO.getAppId()).getAppName());
            jobDefinitionVO.setCron(jobDefinitionDO.getCron());
            jobDefinitionVO.setHandlerName(jobDefinitionDO.getHandlerName());
            jobDefinitionVO.setEnable(jobDefinitionDO.getEnable());
            jobDefinitionVO.setJobName(jobDefinitionDO.getJobName());
            res.add(jobDefinitionVO);
        });
        return res;
    }

    public List<JobInstanceVO> jobInstanceList(JobInstanceQuery jobInstanceQuery) {
        jobInstanceQuery.setPageIndex((jobInstanceQuery.getPageIndex() - 1) * jobInstanceQuery.getPageSize());

        List<JobInstanceDO> list = jobMapper.jobInstanceList(jobInstanceQuery);

        List<JobInstanceVO> res = new ArrayList<>();

        list.stream().forEach(jobInstanceDO -> {
            JobInstanceVO jobInstanceVO = new JobInstanceVO();
            jobInstanceVO.setStartTime(jobInstanceDO.getStartTime());
            jobInstanceVO.setEndTime(jobInstanceDO.getEndTime());
            jobInstanceVO.setTriggerResult(jobInstanceDO.getTriggerResult());
            jobInstanceVO.setTriggerWorker(jobInstanceDO.getTriggerWorker());
            jobInstanceVO.setTriggerType(jobInstanceDO.getTriggerType());
            res.add(jobInstanceVO);
        });
        return res;
    }
}