package com.sumu.jobscheduler.scheduler.core.store;

import org.quartz.impl.jdbcjobstore.Constants;
import org.quartz.impl.jdbcjobstore.StdJDBCConstants;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-16 19:55
 * @desc 自定义SQL
 */
public interface SelfConstants extends StdJDBCConstants, Constants {

    String JOB_APP = "JOB_APP";

    String EXECUTOR_WORKER_ZERO = "当前任务可调度机器为0";

    String SELECT_NEXT_TRIGGER_TO_ACQUIRE = "SELECT "
            + COL_TRIGGER_NAME + ", " + COL_TRIGGER_GROUP + ", "
            + COL_NEXT_FIRE_TIME + ", " + COL_PRIORITY + " FROM "
            + "{0}" + TABLE_TRIGGERS + " WHERE "
            + "{1}" + " AND "
            + COL_SCHEDULER_NAME + " = " + "{2}"
            + " AND " + COL_TRIGGER_STATE + " = ? AND " + COL_NEXT_FIRE_TIME + " <= ? "
            + "AND (" + COL_MISFIRE_INSTRUCTION + " = -1 OR (" + COL_MISFIRE_INSTRUCTION + " != -1 AND " + COL_NEXT_FIRE_TIME + " >= ?)) "
            + "ORDER BY " + COL_NEXT_FIRE_TIME + " ASC, " + COL_PRIORITY + " DESC";

    String SELF_INSERT_TRIGGER = "INSERT INTO "
            + TABLE_PREFIX_SUBST + TABLE_TRIGGERS + " (" + COL_SCHEDULER_NAME + ", " + COL_TRIGGER_NAME
            + ", " + COL_TRIGGER_GROUP + ", " + COL_JOB_NAME + ", "
            + COL_JOB_GROUP + ", " + COL_DESCRIPTION
            + ", " + COL_NEXT_FIRE_TIME + ", " + COL_PREV_FIRE_TIME + ", "
            + COL_TRIGGER_STATE + ", " + COL_TRIGGER_TYPE + ", "
            + COL_START_TIME + ", " + COL_END_TIME + ", " + COL_CALENDAR_NAME
            + ", " + COL_MISFIRE_INSTRUCTION + ", " + COL_JOB_DATAMAP + ", " + COL_PRIORITY + "," + JOB_APP + ") "
            + " VALUES(" + SCHED_NAME_SUBST + ", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

}
