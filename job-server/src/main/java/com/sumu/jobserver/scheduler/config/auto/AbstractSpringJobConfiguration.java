package com.sumu.jobserver.scheduler.config.auto;

import org.apache.ibatis.session.SqlSessionFactory;

import javax.sql.DataSource;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-08 17:09
 */
public abstract class AbstractSpringJobConfiguration {

    protected DataSource dataSource;

    protected SqlSessionFactory sqlSessionFactory;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}
