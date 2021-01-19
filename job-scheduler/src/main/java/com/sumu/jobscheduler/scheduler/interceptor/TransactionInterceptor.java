package com.sumu.jobscheduler.scheduler.interceptor;

import com.sumu.jobscheduler.scheduler.interceptor.command.Command;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-09 15:23
 */
public class TransactionInterceptor extends AbstractCommandInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionInterceptor.class);

    private PlatformTransactionManager transactionManager;

    public TransactionInterceptor(PlatformTransactionManager platformTransactionManager) {
        this.transactionManager = platformTransactionManager;
    }

    @Override
    public <T> T execute(Command<T> command) {
        LOGGER.debug("Running command with propagation REQUIRED");
        TransactionTemplate transactionTemplate = new TransactionTemplate(this.transactionManager);
        //设置事务传播
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        //
        transactionTemplate.setIsolationLevel(TransactionIsolationLevel.REPEATABLE_READ.getLevel());
        T result = transactionTemplate.execute(new TransactionCallback<T>() {
            public T doInTransaction(TransactionStatus status) {
                return TransactionInterceptor.this.next.execute(command);
            }
        });
        return result;
    }
}
