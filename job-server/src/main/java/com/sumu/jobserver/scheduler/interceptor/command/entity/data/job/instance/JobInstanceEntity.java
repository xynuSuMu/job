package com.sumu.jobserver.scheduler.interceptor.command.entity.data.job.instance;

import java.util.Date;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-12 16:10
 */
public interface JobInstanceEntity  extends  JobInstance{

    void setId(int id);

    void setJobDefinitionId(int jobDefinitionId);

    void setStartTime(Date startTime);

    void setEndTime(Date endTime);

    void setTriggerType(int triggerType);

    void setTriggerWorker(String triggerWorker);

    void setTriggerResult(int triggerResult);

}
