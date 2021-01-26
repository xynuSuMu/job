package com.sumu.jobserver.api.service;

import com.sumu.jobscheduler.scheduler.context.JobApplicationContext;
import com.sumu.jobserver.api.vo.JobDefinitionVO;
import com.sumu.jobserver.api.vo.JobInstanceVO;
import com.sumu.jobserver.api.vo.Page;
import com.sumu.jobserver.api.vo.dag.*;
import com.sumu.jobserver.api.vo.param.AddJobVO;
import com.sumu.jobserver.api.vo.param.JavaJobVO;
import com.sumu.jobserver.api.vo.param.ShellJobVO;
import com.sumu.jobserver.api.vo.query.JobDefinitionQuery;
import com.sumu.jobserver.api.vo.query.JobInstanceQuery;
import com.sumu.jobscheduler.scheduler.core.schedule.JobDispatcher;
import com.sumu.jobscheduler.scheduler.core.schedule.JobSchedule;
import com.sumu.jobscheduler.scheduler.core.service.JobApplicationService;
import com.sumu.jobscheduler.scheduler.core.service.JobDefinitionService;
import com.sumu.jobscheduler.scheduler.core.service.JobInstanceService;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.app.App;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.JobDefinition;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.definition.java.JavaJobDefinition;
import com.sumu.jobscheduler.scheduler.interceptor.command.entity.data.job.instance.JobInstance;
import com.sumu.jobscheduler.scheduler.modal.enume.JavaJobInfo;
import com.sumu.jobscheduler.scheduler.modal.enume.JobInfo;
import com.sumu.jobscheduler.scheduler.exception.JobException;
import com.sumu.jobscheduler.scheduler.exception.JobExceptionInfo;
import org.quartz.SchedulerException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
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
    private JobApplicationService jobApplicationService;

    @Autowired
    private JobDefinitionService jobDefinitionService;

    @Autowired
    private JobInstanceService jobInstanceService;

    @Autowired
    private JobSchedule jobSchedule;

    @Autowired
    private JobDispatcher jobDispatcher;


    @Value("${job.specialApp}")
    private String specialApp;

    @Transactional(rollbackFor = Exception.class)
    public void addJob(AddJobVO addJobVO) throws SchedulerException, SQLException {
        //
        App appDO = jobApplicationService.createAppQuery()
                .id(addJobVO.getAppId())
                .singleResult();
        Assert.isTrue(appDO != null, "当前应用不存在");
        String jobName = addJobVO.getJobName();
        int count = jobDefinitionService.createQuery()
                .jobName(jobName)
                .count();
        Assert.isTrue(count == 0, "当前任务名称已存在");
        if (addJobVO.getPostDefinitionID() != null && !"".equals(addJobVO.getPostDefinitionID())) {
            //todo：判断是否是有向无环图
            Set<String> set = new HashSet<>();
            String[] ids = addJobVO.getPostDefinitionID().split(",");

        }
        JobDefinition jobDefinition = jobDefinitionService.createBuilder()
                .appId(addJobVO.getAppId())
                .jobName(addJobVO.getJobName())
                .jobDesc(addJobVO.getJobDesc())
                .taskType(addJobVO.getTaskType())
                .cron(addJobVO.getCron())
                .enable(addJobVO.getEnable())
                .postDefinitionID(addJobVO.getPostDefinitionID())
                .deploy();
        //任务类型分类
        if (addJobVO.getTaskType() == JobInfo.Type.JAVA.getCode()) {
            JavaJobVO javaJobVO = addJobVO.getJavaJobVO();
            if (javaJobVO.getStrategy() == JavaJobInfo.Strategy.SHARD.getCode()) {
                Assert.isTrue(javaJobVO.getStrategy() > 0, "分片数量不合法");
            }
            JavaJobDefinition javaJobDefinition = jobDefinitionService.createJavaBuilder()
                    .definitionID(jobDefinition.getId())
                    .handlerName(javaJobVO.getHandlerName())
                    .strategy(javaJobVO.getStrategy())
                    .shardNum(javaJobVO.getShardNum())
                    .deploy();
        } else if (addJobVO.getTaskType() == JobInfo.Type.SHELL.getCode()) {
            ShellJobVO shellJobVO = addJobVO.getShellJobVO();
            Boolean isSuccess = jobDefinitionService.createShellBuilder()
                    .definitionID(jobDefinition.getId())
                    .user(shellJobVO.getUser())
                    .host(shellJobVO.getHost())
                    .port(shellJobVO.getPort())
                    .pwd(shellJobVO.getPassword())
                    .directory(shellJobVO.getDirectory())
                    .file(shellJobVO.getFile())
                    .param(shellJobVO.getParam())
                    .create();
            if (!isSuccess) {
                throw new JobException(JobExceptionInfo.SHELL_JOB_INSERT_FAIL);
            }
        }
        //增加到调度中心
        if (addJobVO.getEnable()) {
            JobApplicationContext.setSpecial(addJobVO.getSpecialWorker());
            jobSchedule.addJob(String.valueOf(jobDefinition.getId()),
                    appDO.getAppCode(),
                    addJobVO.getCron());
        }
    }

    private void dfs(Set<String> set, String[] ids) {
        for (String id : ids) {
            if (set.contains(id)) {
                throw new JobException(JobExceptionInfo.DAG_CIRCLE);
            }
            set.add(id);
            String postJobIds =
                    jobDefinitionService.createQuery()
                            .id(Integer.valueOf(id))
                            .singleResult()
                            .getPostDefinitionID();
            if (postJobIds == null || "".equals(postJobIds)) {
                continue;
            } else {
                dfs(set, postJobIds.split(","));
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void editJob(AddJobVO addJobVO) throws SchedulerException, SQLException {
        //
        Assert.isTrue(addJobVO.getId() != 0, "JobDefinitionId不合法");
        //
        JobDefinition jobDefinitionDO =
                jobDefinitionService.createQuery().id(addJobVO.getId()).singleResult();
        //
        Assert.isTrue(jobDefinitionDO != null, "JobDefinitionId不存在");
        //删除
        jobDefinitionService.
                createBuilder()
                .id(jobDefinitionDO.getId())
                .delete();
        App appDO = jobApplicationService.createAppQuery().id(addJobVO.getId()).singleResult();
        jobSchedule.removeIfExist(String.valueOf(jobDefinitionDO.getId()), appDO.getAppCode());
        //新增
        addJob(addJobVO);
    }

    @Transactional(rollbackFor = Exception.class)
    public void pause(int id) throws SchedulerException {
        JobDefinition jobDefinitionDO =
                jobDefinitionService.createQuery().id(id).singleResult();
        Assert.isTrue(jobDefinitionDO != null, "当前任务不存在");
        App appDO = jobApplicationService.createAppQuery().id(jobDefinitionDO.getAppId())
                .singleResult();
        jobDefinitionService.createBuilder()
                .enable(false)
                .id(id)
                .deploy();
        jobSchedule.pauseJob(String.valueOf(jobDefinitionDO.getId()), appDO.getAppCode());
    }

    @Transactional(rollbackFor = Exception.class)
    public void trigger(int id) throws SchedulerException {
        //
        jobDispatcher.schedule(String.valueOf(id));
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(int id) throws SchedulerException {
        JobDefinition jobDefinitionDO =
                jobDefinitionService.createQuery().id(id).singleResult();
        Assert.isTrue(jobDefinitionDO != null, "当前任务不存在");
        //删除
        jobDefinitionService.createBuilder().id(id).delete();
        App appDO = jobApplicationService.createAppQuery().id(jobDefinitionDO.getAppId())
                .singleResult();
        Assert.isTrue(appDO != null, "当前任务应用不存在");
        jobSchedule.removeIfExist(String.valueOf(jobDefinitionDO.getId()), appDO.getAppCode());
        return true;
    }


    @Transactional(rollbackFor = Exception.class)
    public void resume(int id) throws SchedulerException {
        JobDefinition jobDefinitionDO =
                jobDefinitionService.createQuery()
                        .id(id)
                        .singleResult();
        Assert.isTrue(jobDefinitionDO != null, "当前任务不存在");
        App appDO = jobApplicationService.createAppQuery().id(jobDefinitionDO.getAppId())
                .singleResult();
        jobDefinitionService.createBuilder()
                .enable(true)
                .id(id)
                .deploy();
        //增加到调度中心
//        jobSchedule.addJob(String.valueOf(jobDefinitionDO.getId()),
//                appDO.getAppCode(),
//                jobDefinitionDO.getCron());
        jobSchedule.resumeJob(String.valueOf(jobDefinitionDO.getId()), appDO.getAppCode());
    }

    public Page<List<JobDefinitionVO>> jobDefinitionList(JobDefinitionQuery jobDefinitionQuery) {
        if (specialApp != null && !"".equals(specialApp)) {
            List<App> list = jobApplicationService
                    .createAppQuery()
                    .appCode(specialApp)
                    .list();
            if (list == null || list.size() == 0)
                return new Page<>();
            jobDefinitionQuery.setAppID(list.get(0).getID());
        }
        int total = jobDefinitionService.createQuery()
                .appId(jobDefinitionQuery.getAppID())
                .count();
        if (total == 0) {
            return new Page<>();
        }
        Page<List<JobDefinitionVO>> res = new Page<>();
        res.setTotal(total);
        res.setCurrent(jobDefinitionQuery.getPageIndex());
        List<JobDefinition> list = jobDefinitionService.createQuery()
                .appId(jobDefinitionQuery.getAppID())
                .index(jobDefinitionQuery.getPageIndex())
                .pageSize(jobDefinitionQuery.getPageSize())
                .list();
        Map<Integer, App> map = jobApplicationService.createAppQuery().list().stream()
                .collect(Collectors.toMap(App::getID, Function.identity())); //APP Info
        List<JobDefinitionVO> list1 = new ArrayList<>();
        list.stream().forEach(jobDefinitionDO -> {
            JobDefinitionVO jobDefinitionVO = new JobDefinitionVO();
            jobDefinitionVO.setId(jobDefinitionDO.getId());
            jobDefinitionVO.setPostDefinitionID(jobDefinitionDO.getPostDefinitionID());
            jobDefinitionVO.setAppName(map.get(jobDefinitionDO.getAppId()).getAppCode());
            jobDefinitionVO.setCron(jobDefinitionDO.getCron());
            jobDefinitionVO.setTaskType(jobDefinitionDO.getTaskType());
            jobDefinitionVO.setEnable(jobDefinitionDO.getEnable());
            jobDefinitionVO.setJobName(jobDefinitionDO.getJobName());
            list1.add(jobDefinitionVO);
        });
        res.setResult(list1);
        return res;
    }

    public JobDefinitionVO jobDefinitionDetail(int id) {

        JobDefinition jobDefinitionDO =
                jobDefinitionService.createQuery().id(id).singleResult();
        //APP Info
        Map<Integer, App> map = jobApplicationService.createAppQuery().list().stream()
                .collect(Collectors.toMap(App::getID, Function.identity()));

        //
        JavaJobDefinition javaJobDO = jobDefinitionService.createJavaQuery()
                .definitionId(id)
                .singleResult();

        JobDefinitionVO jobDefinitionVO = new JobDefinitionVO();
        jobDefinitionVO.setId(jobDefinitionDO.getId());
        if (map.containsKey(jobDefinitionDO.getAppId())) {
            jobDefinitionVO.setAppName(map.get(jobDefinitionDO.getAppId()).getAppCode());
            jobDefinitionVO.setAppId(map.get(jobDefinitionDO.getAppId()).getID());
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

    public Page<List<JobInstanceVO>> jobInstanceList(JobInstanceQuery jobInstanceQuery) {
        Page<List<JobInstanceVO>> page = new Page<>();
        int count = jobInstanceService.createQuery()
                .jobDefinitionId(jobInstanceQuery.getJonDefinitionID())
                .count();
        if (count == 0)
            return page;
        page.setTotal(count);
        List<JobInstance> list = jobInstanceService.createQuery()
                .jobDefinitionId(jobInstanceQuery.getJonDefinitionID())
                .index(jobInstanceQuery.getPageIndex())
                .pageSize(jobInstanceQuery.getPageSize())
                .list();

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
        page.setResult(res);
        return page;
    }

    public DagVO getJobDefinitionDAG(int jobDefinitionID) {
        DagVO dagVO = new DagVO();
        OptionVO optionVO = new OptionVO();
        SeriesVO seriesVO = new SeriesVO();
        List<DataVO> data = new ArrayList<>();
        List<LinksVO> links = new ArrayList<>();
        LinkedList<Integer> queue = new LinkedList<>();
        queue.push(jobDefinitionID);
        doDAG(queue, data, links);
        seriesVO.setData(data);
        seriesVO.setLinks(links);
        optionVO.setSeries(seriesVO);
        dagVO.setOption(optionVO);
        return dagVO;
    }

    public void doDAG(LinkedList<Integer> queue, List<DataVO> data, List<LinksVO> links) {
        Map<Integer, JobDefinition> map = new HashMap<>();
        //Graph
        Map<Integer, List<Integer>> graph = new LinkedHashMap<>();
        while (!queue.isEmpty()) {
            Integer id = queue.pop();
            if (map.containsKey(id))
                continue;

            if (!graph.containsKey(id)) {
                graph.put(id, new ArrayList<>());
            }
            JobDefinition jobDefinitionDO =
                    jobDefinitionService.createQuery().id(id).singleResult();
            map.put(id, jobDefinitionDO);
            String postJobIds = jobDefinitionDO.getPostDefinitionID();
            if (postJobIds != null && !"".equals(postJobIds)) {
                String[] ids = postJobIds.split(",");
                for (String refID : ids) {
                    if (!map.containsKey(refID)) {
                        queue.push(Integer.valueOf(refID));
                    }
                    graph.get(id).add(Integer.valueOf(refID));
                }
            }
        }
        //graph -> DAG
        Set<Map.Entry<Integer, List<Integer>>> set = graph.entrySet();
        long x = 300;
        long y = 300;
        Map<Integer, Integer> visitor = new HashMap<>();
        int source = 0;
        for (Map.Entry<Integer, List<Integer>> entry : set) {
            Integer id = entry.getKey();
            if (!visitor.containsKey(id)) {
                DataVO dataVO = new DataVO(
                        map.get(id).getJobName(),
                        x,
                        y);
                data.add(dataVO);
            } else {
                source = visitor.get(id);
            }
            List<Integer> refIDs = entry.getValue();
            if (refIDs.size() > 0) {
                int temp = 0;
                for (Integer i : refIDs) {
                    if (!visitor.containsKey(i)) {
                        DataVO refDataVO = new DataVO(
                                map.get(i).getJobName(),
                                x + 50,
                                y + (50 * (temp++)));
                        data.add(refDataVO);
                        LinksVO linksVO = new LinksVO(source, data.size() - 1);
                        links.add(linksVO);
                        visitor.put(i, data.size() - 1);
                    } else {
                        LinksVO linksVO = new LinksVO(source, visitor.get(i));
                        links.add(linksVO);
                    }
                }
            }
            x = x + 50;
            if (!visitor.containsKey(id)) {

                visitor.put(id, source);
            }
        }
    }

}
