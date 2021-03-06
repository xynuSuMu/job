package com.sumu.jobserver.api.vo.param;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-22 19:40
 */
public class AddJobVO {
    private int id;
    //应用
    private int appId;
    //名称
    private String jobName;
    //任务描述
    private String jobDesc;
    //任务类型 1-Java 2-Shell
    private int taskType;

    private JavaJobVO javaJobVO;

    private ShellJobVO shellJobVO;

    private String cron;

    private Boolean enable;


    //后置任务
    private String postDefinitionID;

    private Boolean specialWorker;

    public ShellJobVO getShellJobVO() {
        return shellJobVO;
    }

    public void setShellJobVO(ShellJobVO shellJobVO) {
        this.shellJobVO = shellJobVO;
    }

    public Boolean getSpecialWorker() {
        return specialWorker;
    }

    public void setSpecialWorker(Boolean specialWorker) {
        this.specialWorker = specialWorker;
    }

    public String getPostDefinitionID() {
        return postDefinitionID;
    }

    public void setPostDefinitionID(String postDefinitionID) {
        this.postDefinitionID = postDefinitionID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public JavaJobVO getJavaJobVO() {
        return javaJobVO;
    }

    public void setJavaJobVO(JavaJobVO javaJobVO) {
        this.javaJobVO = javaJobVO;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }


    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }


}
