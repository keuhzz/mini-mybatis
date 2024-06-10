package com.zyuhoo.mini.mybatis.executor.statement;

import com.zyuhoo.mini.mybatis.executor.Executor;
import com.zyuhoo.mini.mybatis.mapping.BoundSql;
import com.zyuhoo.mini.mybatis.mapping.MappedStatement;
import com.zyuhoo.mini.mybatis.session.ResultHandler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * About PreparedStatementHandler.
 *
 * @since 0.0.1
 */
public class PreparedStatementHandler extends BaseStatementHandler {

    public PreparedStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject,
        BoundSql boundSql) {
        super(executor, mappedStatement, parameterObject, boundSql);
    }

    @Override
    protected Statement instantiateStatement(Connection connection) throws SQLException {
        String sql = boundSql.getSql();
        return connection.prepareStatement(sql);
    }

    @Override
    public void parameterize(Statement statement) throws SQLException {
        // TODO 硬编码, 后续改造
        PreparedStatement ps = (PreparedStatement) statement;
        Object[] parameterArr = (Object[]) parameterObject;
        ps.setLong(1, Long.parseLong(parameterArr[0].toString()));
    }

    @Override
    public <E> List<E> query(Statement statement, ResultHandler resultHandler) throws SQLException {
        PreparedStatement ps = (PreparedStatement) statement;
        ps.execute();
        return resultSetHandler.handleResultSets(ps);
    }
}
