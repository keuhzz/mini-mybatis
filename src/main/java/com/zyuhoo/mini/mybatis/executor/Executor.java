package com.zyuhoo.mini.mybatis.executor;

import com.zyuhoo.mini.mybatis.mapping.BoundSql;
import com.zyuhoo.mini.mybatis.mapping.MappedStatement;
import com.zyuhoo.mini.mybatis.session.ResultHandler;
import com.zyuhoo.mini.mybatis.transaction.Transaction;
import java.sql.SQLException;
import java.util.List;

/**
 * About Executor.
 *
 * @since 0.0.1
 */
public interface Executor {

    ResultHandler NO_RESULT_HANDLER = null;

    <E> List<E> query(MappedStatement ms, Object parameter, ResultHandler resultHandler, BoundSql boundSql);

    Transaction getTransaction();

    void commit(boolean required) throws SQLException;

    void rollback(boolean required) throws SQLException;

    void close(boolean forceRollback);

}
