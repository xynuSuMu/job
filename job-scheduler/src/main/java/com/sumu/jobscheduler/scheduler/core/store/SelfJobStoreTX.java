package com.sumu.jobscheduler.scheduler.core.store;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumu.jobscheduler.scheduler.config.properties.JobProperties;
import com.sumu.jobscheduler.scheduler.context.JobApplicationContext;
import com.sumu.jobscheduler.scheduler.modal.zk.WorkerDataModal;
import com.sumu.jobscheduler.util.SpringContextUtils;
import org.quartz.*;
import org.quartz.impl.jdbcjobstore.*;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.spi.OperableTrigger;
import org.quartz.spi.SchedulerSignaler;
import org.quartz.utils.ConnectionProvider;
import org.quartz.utils.DBConnectionManager;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.Date;

import static com.sumu.common.core.ZKConstants.NODE_REGISTER;
import static org.quartz.TriggerKey.triggerKey;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-01 17:08
 */
public class SelfJobStoreTX extends JobStoreCMT {

    public static final String TX_DATA_SOURCE_PREFIX = "springTxDataSource.";

    public static final String NON_TX_DATA_SOURCE_PREFIX = "springNonTxDataSource.";

    private SelfJDBCDelegate delegate;

    protected Class<SelfJDBCDelegate> delegateClass = SelfJDBCDelegate.class;

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
            } catch (MetaDataAccessException e) {
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
        //todo：分布式锁
        return super.acquireNextTriggers(noLaterThan, maxCount, timeWindow);
    }

    protected SelfJDBCDelegate getDelegate() throws NoSuchDelegateException {
        synchronized (this) {
            if (null == delegate) {
                try {
                    JobProperties jobProperties = SpringContextUtils.getBean(JobProperties.class);
                    String appJob = jobProperties.getSpecialApp();
                    delegate = delegateClass.newInstance();
                    delegate.selfInitialize(getLog(), tablePrefix, instanceName, instanceId,
                            getClassLoadHelper(), canUseProperties(), getDriverDelegateInitString(),
                            appJob);
                } catch (InstantiationException e) {
                    throw new NoSuchDelegateException("Couldn't create delegate: "
                            + e.getMessage(), e);
                } catch (IllegalAccessException e) {
                    throw new NoSuchDelegateException("Couldn't create delegate: "
                            + e.getMessage(), e);
                }
            }
            return delegate;
        }
    }


    @Override
    protected List<OperableTrigger> acquireNextTrigger(Connection conn,
                                                       long noLaterThan,
                                                       int maxCount,
                                                       long timeWindow) throws JobPersistenceException {
        String appJob = getDelegate().getApp();
        List<String> list = null;
        try {
            if (appJob == null || "".equals(appJob))
                list = JobApplicationContext.getClient().getChildren().forPath(NODE_REGISTER);
            else
                list = JobApplicationContext.getClient().getChildren().forPath(NODE_REGISTER + "/" + appJob);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (list == null || list.size() == 0) {
            return new ArrayList();
        }
        String currentNodeIP;
        String path;
        //获取当前机器编号
        int c = 0;
        for (String nodeName : list) {
            currentNodeIP = JobApplicationContext.getIP();
            if (appJob == null || "".equals(appJob))
                path = NODE_REGISTER + "/" + nodeName;
            else
                path = NODE_REGISTER + "/" + appJob + "/" + nodeName;
            try {
                byte[] data = JobApplicationContext.getClient().getData().forPath(path);
                WorkerDataModal workerDataModal = new ObjectMapper().readValue(data, WorkerDataModal.class);
                c++;
                if (currentNodeIP.equals(workerDataModal.getIp())) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (c == 0) {
            return new ArrayList<>();
        }
        //总机器数量
        int all = list.size();
        if (timeWindow < 0L) {
            throw new IllegalArgumentException();
        } else {
            List<OperableTrigger> acquiredTriggers = new ArrayList();
            Set<JobKey> acquiredJobKeysForNoConcurrentExec = new HashSet();
            int currentLoopCount = 0;
            while (true) {
                ++currentLoopCount;
                try {
                    //重写获取noLaterThan + timeWindow范围内的Trigger
                    List<TriggerKey> keys = this.getDelegate().selectTriggerToAcquire(conn,
                            noLaterThan + timeWindow,
                            this.getMisfireTime(),
                            maxCount);
                    if (keys != null && keys.size() != 0) {
                        long batchEnd = noLaterThan;
                        Iterator i$ = keys.iterator();
                        while (i$.hasNext()) {
                            TriggerKey triggerKey = (TriggerKey) i$.next();
                            //计算hash
                            int hash = triggerKey.getName().hashCode() % all;
                            this.getLog().info("Name = " + triggerKey.getName() + "---Group=" + triggerKey.getGroup() + "--Hash=" + hash);
                            if (all == 1 || hash == c) {
                                OperableTrigger nextTrigger = this.retrieveTrigger(conn, triggerKey);
                                if (nextTrigger != null) {
                                    JobKey jobKey = nextTrigger.getJobKey();
                                    JobDetail job;
                                    try {
                                        job = this.retrieveJob(conn, jobKey);
                                    } catch (JobPersistenceException e) {
                                        JobPersistenceException jpe = e;
                                        try {
                                            this.getLog().error("Error retrieving job, setting trigger state to ERROR.", jpe);
                                            this.getDelegate().updateTriggerState(conn, triggerKey, "ERROR");
                                        } catch (SQLException e1) {
                                            this.getLog().error("Unable to set trigger state to ERROR.", e1);
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
                return acquiredTriggers;
            }
        }
    }

}
