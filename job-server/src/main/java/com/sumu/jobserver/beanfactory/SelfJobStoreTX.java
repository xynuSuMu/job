package com.sumu.jobserver.beanfactory;

import org.quartz.*;
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
import java.util.*;

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
            this.setDataSource(TX_DATA_SOURCE_PREFIX + this.getInstanceName());
            this.setDontSetAutoCommitFalse(true);
            DBConnectionManager.getInstance().addConnectionProvider(TX_DATA_SOURCE_PREFIX + this.getInstanceName(), new ConnectionProvider() {
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
            this.setNonManagedTXDataSource(NON_TX_DATA_SOURCE_PREFIX + this.getInstanceName());
            DBConnectionManager.getInstance().addConnectionProvider(NON_TX_DATA_SOURCE_PREFIX + this.getInstanceName(), new ConnectionProvider() {
                public Connection getConnection() throws SQLException {
                    return nonTxDataSourceToUse.getConnection();
                }

                public void shutdown() {
                }

                public void initialize() {
                }
            });

            try {
                String productName = JdbcUtils.extractDatabaseMetaData(this.dataSource, DatabaseMetaData::getDatabaseProductName);
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

        //分布式锁


        return super.acquireNextTriggers(noLaterThan, maxCount, timeWindow);
    }

    @Override
    protected List<OperableTrigger> acquireNextTrigger(Connection conn, long noLaterThan, int maxCount, long timeWindow) throws JobPersistenceException {

        //总机器数量
        int all = 10;

        //获取当前机器编号
        int c = 5;
        this.getLog().info("执行自定义acquireNextTrigger");
        if (timeWindow < 0L) {
            throw new IllegalArgumentException();
        } else {
            List<OperableTrigger> acquiredTriggers = new ArrayList();
            Set<JobKey> acquiredJobKeysForNoConcurrentExec = new HashSet();
            int currentLoopCount = 0;
            while (true) {
                this.getLog().info("执行自定义acquireNextTrigger2");
                ++currentLoopCount;
                try {
                    List<TriggerKey> keys = this.getDelegate().selectTriggerToAcquire(conn, noLaterThan + timeWindow, this.getMisfireTime(), maxCount);
                    if (keys != null && keys.size() != 0) {
                        long batchEnd = noLaterThan;
                        Iterator i$ = keys.iterator();
                        while (i$.hasNext()) {
                            TriggerKey triggerKey = (TriggerKey) i$.next();
                            //计算hash
                            int hash = triggerKey.getName().hashCode() % all;
                            this.getLog().info(triggerKey.getName() + "---" + triggerKey.getGroup()+"--"+hash);

                            if (hash == c) {
                                this.getLog().info("归属本机执行");
                                OperableTrigger nextTrigger = this.retrieveTrigger(conn, triggerKey);
                                if (nextTrigger != null) {
                                    JobKey jobKey = nextTrigger.getJobKey();
                                    JobDetail job;
                                    try {
                                        job = this.retrieveJob(conn, jobKey);
                                    } catch (JobPersistenceException var22) {
                                        JobPersistenceException jpe = var22;

                                        try {
                                            this.getLog().error("Error retrieving job, setting trigger state to ERROR.", jpe);
                                            this.getDelegate().updateTriggerState(conn, triggerKey, "ERROR");
                                        } catch (SQLException var21) {
                                            this.getLog().error("Unable to set trigger state to ERROR.", var21);
                                        }
                                        continue;
                                    }

                                    if (job.isConcurrentExectionDisallowed()) {
                                        if (acquiredJobKeysForNoConcurrentExec.contains(jobKey)) {
                                            continue;
                                        }

                                        acquiredJobKeysForNoConcurrentExec.add(jobKey);
                                    }

                                    Date nextFireTime = nextTrigger.getNextFireTime();
                                    if (nextFireTime == null) {
                                        this.getLog().warn("Trigger {} returned null on nextFireTime and yet still exists in DB!", nextTrigger.getKey());
                                    } else {
                                        if (nextFireTime.getTime() > batchEnd) {
                                            break;
                                        }

                                        int rowsUpdated = this.getDelegate().updateTriggerStateFromOtherState(conn, triggerKey, "ACQUIRED", "WAITING");
                                        if (rowsUpdated > 0) {
                                            nextTrigger.setFireInstanceId(this.getFiredTriggerRecordId());
                                            this.getDelegate().insertFiredTrigger(conn, nextTrigger, "ACQUIRED", (JobDetail) null);
                                            if (acquiredTriggers.isEmpty()) {
                                                batchEnd = Math.max(nextFireTime.getTime(), System.currentTimeMillis()) + timeWindow;
                                            }
                                            acquiredTriggers.add(nextTrigger);
                                        }
                                    }
                                }
                            } else {
                                this.getLog().info("不归属本机执行!");
                            }

                        }
                    }
                } catch (Exception e) {
                    throw new JobPersistenceException("Couldn't acquire next trigger: " + e.getMessage(), e);
                }
                if (acquiredTriggers.size() == 0 && currentLoopCount < 3) {
                    continue;
                }
                this.getLog().info("执行自定义acquireNextTrigger3");
                return acquiredTriggers;
            }
        }
    }

}
