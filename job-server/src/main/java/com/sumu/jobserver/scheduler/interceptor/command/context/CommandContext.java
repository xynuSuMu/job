package com.sumu.jobserver.scheduler.interceptor.command.context;

import com.sumu.jobserver.scheduler.config.auto.SpringJobConfiguration;
import com.sumu.jobserver.scheduler.interceptor.command.Command;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2021-01-08 10:39
 */
public class CommandContext {

    private Logger LOG = LoggerFactory.getLogger(CommandContext.class);

    protected Command<?> command;

    protected Throwable exception;

    protected SpringJobConfiguration springJobConfiguration;

    protected SqlSessionFactory sqlSessionFactory;

    protected SqlSession sqlSession;

    protected LinkedList<Object> resultStack = new LinkedList();

    protected Boolean open = false;

    public CommandContext(Command<?> command, SpringJobConfiguration springJobConfiguration) {
        this.command = command;
        this.springJobConfiguration = springJobConfiguration;
        this.sqlSessionFactory = springJobConfiguration.getSqlSessionFactory();
    }


    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public void setResult(Object result) {
        this.resultStack.add(result);
    }

    public Object getResult() {
        return this.resultStack.pollLast();
    }

    protected Boolean openSqlSession() {
        return open;
    }

    public SqlSession getSqlSession() {
        if (sqlSession == null) {
            synchronized (this) {
                if (sqlSession == null) {
                    LOG.info("[JOB] Open SqlSession");
                    this.sqlSession = sqlSessionFactory.openSession();
                    this.open = true;
                }
            }
        }
        return sqlSession;
    }

    public void close() {
        try {
            if (openSqlSession()) {
                if (exception == null) {
//                    this.sqlSession.commit();
                } else {
//                    this.sqlSession.rollback();
                    //Spring
                    this.springJobConfiguration.getTransactionManager()
                            .getTransaction(null)
                            .setRollbackOnly();

                }
            }
        } finally {
            if (openSqlSession())
                closeSqlSession();
        }

    }

    private void closeSqlSession() {
        LOG.info("[JOB] close SqlSession");
        this.sqlSession.close();
    }
}
