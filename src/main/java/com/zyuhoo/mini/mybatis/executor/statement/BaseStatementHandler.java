package com.zyuhoo.mini.mybatis.executor.statement;

import com.zyuhoo.mini.mybatis.executor.Executor;
import com.zyuhoo.mini.mybatis.executor.resultset.ResultSetHandler;
import com.zyuhoo.mini.mybatis.mapping.BoundSql;
import com.zyuhoo.mini.mybatis.mapping.MappedStatement;
import com.zyuhoo.mini.mybatis.session.Configuration;
import com.zyuhoo.mini.mybatis.session.ResultHandler;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * About BaseStatementHandler.
 *
 * @since 0.0.1
 */
public abstract class BaseStatementHandler implements StatementHandler {

    protected final Configuration configuration;

    protected final Executor executor;

    protected final MappedStatement mappedStatement;

    protected final Object parameterObject;

    protected final ResultSetHandler resultSetHandler;

    protected BoundSql boundSql;

    public BaseStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject,
        BoundSql boundSql) {
        this.configuration = mappedStatement.getConfiguration();
        this.executor = executor;
        this.mappedStatement = mappedStatement;
        this.boundSql = boundSql;
        this.parameterObject = parameterObject;
        this.resultSetHandler = configuration.newResultSetHandler(executor, mappedStatement, boundSql);
    }


    @Override
    public Statement prepare(Connection connection) throws SQLException {
        Statement statement;
        try {
            // 实例化
            statement = instantiateStatement(connection);
            // 参数设置
            statement.setQueryTimeout(600);
            statement.setFetchSize(10000);
            return statement;
        } catch (SQLException e) {
            throw new RuntimeException("Error preparing statement. Cause: " + e);
        }
    }

    protected abstract Statement instantiateStatement(Connection connection) throws SQLException;
}
