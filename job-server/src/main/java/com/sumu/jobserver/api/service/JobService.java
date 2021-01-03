package com.sumu.jobserver.api.service;

import com.sumu.jobserver.api.vo.JobDefinitionVO;
import com.sumu.jobserver.api.vo.JobInstanceVO;
import com.sumu.jobserver.api.vo.param.AddJobVO;
import com.sumu.jobserver.api.vo.param.JavaJobVO;
import com.sumu.jobserver.api.vo.query.JobDefinitionQuery;
import com.sumu.jobserver.api.vo.query.JobInstanceQuery;
import com.sumu.jobserver.core.schedule.JobSchedule;
import com.sumu.jobserver.enume.JavaJobInfo;
import com.sumu.jobserver.enume.JobInfo;
import com.sumu.jobserver.mapper.AppMapper;
import com.sumu.jobserver.mapper.JobMapper;
import com.sumu.jobserver.modal.app.AppDO;
import com.sumu.jobserver.modal.job.JavaJobDO;
import com.sumu.jobserver.modal.job.JobDefinitionDO;
import com.sumu.jobserver.modal.job.JobInstanceDO;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.text.SimpleDateFormat;
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
        //任务类型分类
        if (addJobVO.getTaskType() == JobInfo.Type.JAVA.getCode()) {
            JavaJobVO javaJobVO = addJobVO.getJavaJobVO();
            if (javaJobVO.getStrategy() == JavaJobInfo.Strategy.SHARD.getCode()) {
                Assert.isTrue(javaJobVO.getStrategy() > 0, "分片数量不合法");
            }
            JavaJobDO javaJobDO = new JavaJobDO();
            BeanUtils.copyProperties(javaJobVO, javaJobDO);
            javaJobDO.setDefinitionID(jobDefinitionDO.getId());
            jobMapper.insertJavaJobDefinition(javaJobDO);
        } else {

        }
        //增加到调度中心
        if (addJobVO.getEnable()) {
            jobSchedule.addJob(String.valueOf(jobDefinitionDO.getId()),
                    appDO.getAppName(),
                    addJobVO.getCron());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void editJob(AddJobVO addJobVO) throws SchedulerException {
        //
        Assert.isTrue(addJobVO.getId() != 0, "JobDefinitionId不合法");
        //
        JobDefinitionDO jobDefinitionDO = jobMapper.getJobDefinitionByID(String.valueOf(addJobVO.getId()));
        //
        Assert.isTrue(jobDefinitionDO != null, "JobDefinitionId不存在");
        //删除
        jobMapper.removeJobDefinition(jobDefinitionDO.getId());
        AppDO appDO = appMapper.getAppById(jobDefinitionDO.getAppId());
        jobSchedule.removeIfExist(String.valueOf(jobDefinitionDO.getId()), appDO.getAppName());
        //新增
        addJob(addJobVO);
    }

    @Transactional(rollbackFor = Exception.class)
    public void pause(int id) throws SchedulerException {
        JobDefinitionDO jobDefinitionDO = jobMapper.getJobDefinitionByID(String.valueOf(id));
        Assert.isTrue(jobDefinitionDO != null, "当前任务不存在");
        AppDO appDO = appMapper.getAppById(jobDefinitionDO.getAppId());
        jobMapper.updateJobDefinitionState(id, false);
        jobSchedule.removeIfExist(String.valueOf(jobDefinitionDO.getId()), appDO.getAppName());
    }

    @Transactional(rollbackFor = Exception.class)
    public void resume(int id) throws SchedulerException {
        JobDefinitionDO jobDefinitionDO = jobMapper.getJobDefinitionByID(String.valueOf(id));
        Assert.isTrue(jobDefinitionDO != null, "当前任务不存在");
        AppDO appDO = appMapper.getAppById(jobDefinitionDO.getAppId());
        jobMapper.updateJobDefinitionState(id, true);
        //增加到调度中心
        jobSchedule.addJob(String.valueOf(jobDefinitionDO.getId()),
                appDO.getAppName(),
                jobDefinitionDO.getCron());
    }

    public List<JobDefinitionVO> jobDefinitionList(JobDefinitionQuery jobDefinitionQuery) {
        jobDefinitionQuery.setPageIndex((jobDefinitionQuery.getPageIndex() - 1) * jobDefinitionQuery.getPageSize());

        List<JobDefinitionDO> list = jobMapper.jobDefinitionList(jobDefinitionQuery);

        if (list == null || list.size() == 0)
            return new ArrayList<>();

//        List<Integer> definitionIds = list.stream().map(jobDefinitionDO -> jobDefinitionDO.getId()).collect(Collectors.toList());
        //APP Info
        Map<Integer, AppDO> map = appMapper.getApps().stream().collect(Collectors.toMap(AppDO::getId, Function.identity()));
        //
//        Map<Integer, JavaJobDO> javaJobDOS = jobMapper.getJavaJobDefinitionByDefIds(definitionIds).stream()
//                .collect((Collectors.toMap(JavaJobDO::getDefinitionID, Function.identity())));
        List<JobDefinitionVO> res = new ArrayList<>();
        list.stream().forEach(jobDefinitionDO -> {
            JobDefinitionVO jobDefinitionVO = new JobDefinitionVO();
            jobDefinitionVO.setId(jobDefinitionDO.getId());
            jobDefinitionVO.setAppName(map.get(jobDefinitionDO.getAppId()).getAppName());
            jobDefinitionVO.setCron(jobDefinitionDO.getCron());
            jobDefinitionVO.setTaskType(jobDefinitionDO.getTaskType());
            jobDefinitionVO.setEnable(jobDefinitionDO.getEnable());
            jobDefinitionVO.setJobName(jobDefinitionDO.getJobName());
            JavaJobVO javaJobVO = new JavaJobVO();
//            if (jobDefinitionDO.getTaskType() == 1 && javaJobDOS.containsKey(jobDefinitionDO.getId())) {
//                BeanUtils.copyProperties(javaJobDOS.get(jobDefinitionDO.getId()), javaJobVO);
//                jobDefinitionVO.setJavaJobVO(javaJobVO);
//            }
            res.add(jobDefinitionVO);
        });
        return res;
    }

    public JobDefinitionVO jobDefinitionDetail(int id) {

        JobDefinitionDO jobDefinitionDO = jobMapper.getJobDefinitionByID(String.valueOf(id));

        //APP Info
        Map<Integer, AppDO> map = appMapper.getApps().stream().collect(Collectors.toMap(AppDO::getId, Function.identity()));
        //
        JavaJobDO javaJobDO = jobMapper.getJavaJobDefinitionByDefId(id);


        JobDefinitionVO jobDefinitionVO = new JobDefinitionVO();
        jobDefinitionVO.setId(jobDefinitionDO.getId());
        if (map.containsKey(jobDefinitionDO.getAppId())) {
            jobDefinitionVO.setAppName(map.get(jobDefinitionDO.getAppId()).getAppName());
            jobDefinitionVO.setAppId(map.get(jobDefinitionDO.getAppId()).getId());
        }
        jobDefinitionVO.setCron(jobDefinitionDO.getCron());
        jobDefinitionVO.setTaskType(jobDefinitionDO.getTaskType());
        jobDefinitionVO.setEnable(jobDefinitionDO.getEnable());
        jobDefinitionVO.setJobName(jobDefinitionDO.getJobName());
        jobDefinitionVO.setJobDesc(jobDefinitionDO.getJobDesc());
        if (jobDefinitionDO.getTaskType() == 1) {
            JavaJobVO javaJobVO = new JavaJobVO();
            BeanUtils.copyProperties(javaJobDO, javaJobVO);
            jobDefinitionVO.setJavaJobVO(javaJobVO);
        }

        return jobDefinitionVO;
    }

    public List<JobInstanceVO> jobInstanceList(JobInstanceQuery jobInstanceQuery) {
        jobInstanceQuery.setPageIndex((jobInstanceQuery.getPageIndex() - 1) * jobInstanceQuery.getPageSize());

        List<JobInstanceDO> list = jobMapper.jobInstanceList(jobInstanceQuery);

        List<JobInstanceVO> res = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        list.stream().forEach(jobInstanceDO -> {
            JobInstanceVO jobInstanceVO = new JobInstanceVO();
            jobInstanceVO.setId(jobInstanceDO.getId());
            jobInstanceVO.setStartTime(jobInstanceDO.getStartTime() != null ? format.format(jobInstanceDO.getStartTime()) : "");
            jobInstanceVO.setEndTime(jobInstanceDO.getEndTime() != null ? format.format(jobInstanceDO.getEndTime()) : "");
            jobInstanceVO.setTriggerResult(jobInstanceDO.getTriggerResult());
            jobInstanceVO.setTriggerWorker(jobInstanceDO.getTriggerWorker());
            jobInstanceVO.setTriggerType(jobInstanceDO.getTriggerType());
            res.add(jobInstanceVO);
        });
        return res;
    }
}
