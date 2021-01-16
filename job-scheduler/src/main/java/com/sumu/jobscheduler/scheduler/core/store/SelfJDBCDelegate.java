package com.sumu.jobscheduler.scheduler.core.store;

import com.sumu.jobscheduler.scheduler.context.JobApplicationContext;
import org.quartz.JobDetail;
import org.quartz.TriggerKey;
import org.quartz.impl.jdbcjobstore.NoSuchDelegateException;
import org.quartz.impl.jdbcjobstore.StdJDBCDelegate;
import org.quartz.impl.jdbcjobstore.TriggerPersistenceDelegate;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.spi.OperableTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

import static com.sumu.jobscheduler.scheduler.core.store.SelfConstants.SELF_INSERT_TRIGGER;
import static org.quartz.TriggerKey.triggerKey;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-16 20:00
 */
public class SelfJDBCDelegate extends StdJDBCDelegate {

    private Logger LOG = LoggerFactory.getLogger(SelfJDBCDelegate.class);

    private String app;


    protected void selfInitialize(Logger logger,
                                  String tablePrefix,
                                  String schedName,
                                  String instanceId,
                                  ClassLoadHelper classLoadHelper,
                                  boolean useProperties,
                                  String initString,
                                  String app
    ) throws NoSuchDelegateException {
        super.initialize(logger, tablePrefix, schedName, instanceId, classLoadHelper, useProperties, initString);
        this.app = app;
    }

    protected String getApp() {
        return app;
    }

    public List<TriggerKey> selectTriggerToAcquire(Connection conn,
                                                   long noLaterThan,
                                                   long noEarlierThan,
                                                   int maxCount)
            throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<TriggerKey> nextTriggers = new LinkedList<TriggerKey>();
        String query = MessageFormat.format(SelfConstants.SELECT_NEXT_TRIGGER_TO_ACQUIRE,
                this.tablePrefix,
                (app == null || "".equals(app)) ? "JOB_APP is null" : "JOB_APP = '" + app + "'",
                getSchedulerNameLiteral());
        LOG.info("QUEERY,{}", query);
        try {
            ps = conn.prepareStatement(query);

            if (maxCount < 1)
                maxCount = 1;
            ps.setMaxRows(maxCount);

            ps.setFetchSize(maxCount);

            ps.setString(1, STATE_WAITING);
            ps.setBigDecimal(2, new BigDecimal(String.valueOf(noLaterThan)));
            ps.setBigDecimal(3, new BigDecimal(String.valueOf(noEarlierThan)));
            rs = ps.executeQuery();

            while (rs.next() && nextTriggers.size() < maxCount) {
                nextTriggers.add(triggerKey(
                        rs.getString(COL_TRIGGER_NAME),
                        rs.getString(COL_TRIGGER_GROUP)));
            }
            LOG.info("查询数量,{}", nextTriggers.size());
            return nextTriggers;
        } finally {
            if (null != rs)
                rs.close();
            if (null != ps)
                ps.close();
        }

    }

    public int insertTrigger(Connection conn, OperableTrigger trigger, String state,
                             JobDetail jobDetail) throws SQLException, IOException {

        ByteArrayOutputStream baos = null;
        if (trigger.getJobDataMap().size() > 0) {
            baos = serializeJobData(trigger.getJobDataMap());
        }
        PreparedStatement ps = null;
        int insertResult = 0;
        try {
            ps = conn.prepareStatement(rtp(SELF_INSERT_TRIGGER));
            ps.setString(1, trigger.getKey().getName());
            ps.setString(2, trigger.getKey().getGroup());
            ps.setString(3, trigger.getJobKey().getName());
            ps.setString(4, trigger.getJobKey().getGroup());
            ps.setString(5, trigger.getDescription());
            if (trigger.getNextFireTime() != null)
                ps.setBigDecimal(6, new BigDecimal(String.valueOf(trigger
                        .getNextFireTime().getTime())));
            else
                ps.setBigDecimal(6, null);
            long prevFireTime = -1;
            if (trigger.getPreviousFireTime() != null) {
                prevFireTime = trigger.getPreviousFireTime().getTime();
            }
            ps.setBigDecimal(7, new BigDecimal(String.valueOf(prevFireTime)));
            ps.setString(8, state);
            TriggerPersistenceDelegate tDel = findTriggerPersistenceDelegate(trigger);
            String type = TTYPE_BLOB;
            if (tDel != null)
                type = tDel.getHandledTriggerTypeDiscriminator();
            ps.setString(9, type);
            ps.setBigDecimal(10, new BigDecimal(String.valueOf(trigger
                    .getStartTime().getTime())));
            long endTime = 0;
            if (trigger.getEndTime() != null) {
                endTime = trigger.getEndTime().getTime();
            }
            ps.setBigDecimal(11, new BigDecimal(String.valueOf(endTime)));
            ps.setString(12, trigger.getCalendarName());
            ps.setInt(13, trigger.getMisfireInstruction());
            setBytes(ps, 14, baos);
            ps.setInt(15, trigger.getPriority());
            if (JobApplicationContext.getSpecial()!=null && JobApplicationContext.getSpecial())
                ps.setString(16, trigger.getKey().getGroup());
            else
                ps.setNull(16, Types.VARCHAR);

            insertResult = ps.executeUpdate();

            if (tDel == null)
                insertBlobTrigger(conn, trigger);
            else
                tDel.insertExtendedTriggerProperties(conn, trigger, state, jobDetail);

        } finally {
            closeStatement(ps);
        }

        return insertResult;
    }


}
