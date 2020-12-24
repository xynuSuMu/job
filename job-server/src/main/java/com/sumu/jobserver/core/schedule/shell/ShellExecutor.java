package com.sumu.jobserver.core.schedule.shell;

import com.sumu.jobserver.core.schedule.AbstractJobExecutor;
import com.sumu.jobserver.modal.job.JobDefinitionDO;
import org.springframework.stereotype.Component;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-24 18:14
 */
@Component
public class ShellExecutor extends AbstractJobExecutor {

    @Override
    public void executorByQuartz(JobDefinitionDO jobDefinitionDO) {

    }
}
