package com.zyuhoo.mini.mybatis.executor;

import com.zyuhoo.mini.mybatis.executor.statement.StatementHandler;
import com.zyuhoo.mini.mybatis.mapping.BoundSql;
import com.zyuhoo.mini.mybatis.mapping.MappedStatement;
import com.zyuhoo.mini.mybatis.session.Configuration;
import com.zyuhoo.mini.mybatis.session.ResultHandler;
import com.zyuhoo.mini.mybatis.transaction.Transaction;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * About SimpleExecutor.
 *
 * @since 0.0.1
 */
public class SimpleExecutor extends BaseExecutor {
    private static final Logger log = LoggerFactory.getLogger(SimpleExecutor.class);

    public SimpleExecutor(Configuration configuration, Transaction transaction) {
        super(configuration, transaction);
    }

    @Override
    protected <E> List<E> doQuery(MappedStatement ms, Object parameter, ResultHandler resultHandler,
        BoundSql boundSql) {
        Statement statement = null;
        try {
            // 1. 获取连接(此处不需要直接关闭连接, 因为此处连接由事务控制, 关闭事务时会同步关闭连接)
            Connection connection = transaction.getConnection();
            // 2. 创建语句处理器
            Configuration configuration = ms.getConfiguration();
            StatementHandler statementHandler = configuration.newStatementHandler(this, ms, parameter, boundSql);
            // 3. 创建, 参数化, 执行语句 prepare-parameterize-query
            statement = statementHandler.prepare(connection);
            statementHandler.parameterize(statement);
            return statementHandler.query(statement, resultHandler);
        } catch (SQLException e) {
            log.error("Executor do query error. Cause: ", e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    log.error("Statement close error. Cause: ", e);
                }
            }
        }
        return null;
    }
}
