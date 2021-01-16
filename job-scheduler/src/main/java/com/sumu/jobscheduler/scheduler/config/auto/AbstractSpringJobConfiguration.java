package com.sumu.jobscheduler.scheduler.config.auto;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-08 17:09
 */
public abstract class AbstractSpringJobConfiguration {

    protected DataSource dataSource;

    protected SqlSessionFactory sqlSessionFactory;

    protected PlatformTransactionManager transactionManager;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        if (dataSource instanceof TransactionAwareDataSourceProxy) {
            this.dataSource = dataSource;
        } else {
            DataSource proxiedDataSource = new TransactionAwareDataSourceProxy(dataSource);
            this.dataSource = proxiedDataSource;
        }
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
}
