package com.sumu.jobserver.beanfactory;

import org.quartz.JobPersistenceException;
import org.quartz.SchedulerConfigException;
import org.quartz.impl.jdbcjobstore.JobStoreCMT;
import org.quartz.impl.jdbcjobstore.SimpleSemaphore;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.spi.OperableTrigger;
import org.quartz.spi.SchedulerSignaler;
import org.quartz.utils.ConnectionProvider;
import org.quartz.utils.DBConnectionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-01 17:08
 */
public class SelfJobStoreTX extends JobStoreCMT {
    public static final String TX_DATA_SOURCE_PREFIX = "springTxDataSource.";
    public static final String NON_TX_DATA_SOURCE_PREFIX = "springNonTxDataSource.";
    @Nullable
    private DataSource dataSource;

    public SelfJobStoreTX() {
    }

    public void initialize(ClassLoadHelper loadHelper, SchedulerSignaler signaler) throws SchedulerConfigException {
        this.dataSource = SchedulerFactoryBean.getConfigTimeDataSource();
        if (this.dataSource == null) {
            throw new SchedulerConfigException("No local DataSource found for configuration - 'dataSource' property must be set on SchedulerFactoryBean");
        } else {
            this.setDataSource("springTxDataSource." + this.getInstanceName());
            this.setDontSetAutoCommitFalse(true);
            DBConnectionManager.getInstance().addConnectionProvider("springTxDataSource." + this.getInstanceName(), new ConnectionProvider() {
                public Connection getConnection() throws SQLException {
                    return DataSourceUtils.doGetConnection(SelfJobStoreTX.this.dataSource);
                }

                public void shutdown() {
                }

                public void initialize() {
                }
            });
            DataSource nonTxDataSource = SchedulerFactoryBean.getConfigTimeNonTransactionalDataSource();
            final DataSource nonTxDataSourceToUse = nonTxDataSource != null ? nonTxDataSource : this.dataSource;
            this.setNonManagedTXDataSource("springNonTxDataSource." + this.getInstanceName());
            DBConnectionManager.getInstance().addConnectionProvider("springNonTxDataSource." + this.getInstanceName(), new ConnectionProvider() {
                public Connection getConnection() throws SQLException {
                    return nonTxDataSourceToUse.getConnection();
                }

                public void shutdown() {
                }

                public void initialize() {
                }
            });

            try {
                String productName = (String) JdbcUtils.extractDatabaseMetaData(this.dataSource, DatabaseMetaData::getDatabaseProductName);
                productName = JdbcUtils.commonDatabaseName(productName);
                if (productName != null && productName.toLowerCase().contains("hsql")) {
                    this.setUseDBLocks(false);
                    this.setLockHandler(new SimpleSemaphore());
                }
            } catch (MetaDataAccessException var6) {
                this.logWarnIfNonZero(1, "Could not detect database type. Assuming locks can be taken.");
            }

            super.initialize(loadHelper, signaler);
        }
    }

    protected void closeConnection(Connection con) {
        DataSourceUtils.releaseConnection(con, this.dataSource);
    }

    @Override
    public List<OperableTrigger> acquireNextTriggers(long noLaterThan, int maxCount, long timeWindow) throws JobPersistenceException {
        //TODO:需要重写的地方，保证集群机器不会闲置
        return super.acquireNextTriggers(noLaterThan, maxCount, timeWindow);
    }
}
