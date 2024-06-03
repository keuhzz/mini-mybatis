package com.zyuhoo.mini.mybatis.session.defaults;

import com.zyuhoo.mini.mybatis.mapping.MappedStatement;
import com.zyuhoo.mini.mybatis.mapping.BoundSql;
import com.zyuhoo.mini.mybatis.session.Configuration;
import com.zyuhoo.mini.mybatis.session.SqlSession;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * SqlSession 默认实现.
 */
public class DefaultSqlSession implements SqlSession {

    private final Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T selectOne(String statement) {
        return (T) ("execute select sql, method: " + statement);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T selectOne(String statement, Object parameter) {
        // TODO demo
        MappedStatement mappedStatement = configuration.getMappedStatement(statement);
        BoundSql boundSql = mappedStatement.getBoundSql();
        String sql = boundSql.getSql();
        T res;
        try {
            Connection connection = configuration.getEnvironment().getDataSource().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, Long.parseLong(((Object[]) parameter)[0].toString()));
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            Class<T> resClass = (Class<T>) Class.forName(boundSql.getResultType());
            res = resClass.newInstance();
            resultSet.next();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                Object columnValue = resultSet.getObject(i);
                String columnName = metaData.getColumnName(i);
                String setMethod = "set" + columnName.substring(0, 1).toUpperCase() + columnName.substring(1);
                Method method = resClass.getMethod(setMethod, columnValue.getClass());
                method.invoke(res, columnValue);
            }
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException
                 | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("execute sql error. cause: " + e, e);
        }
        return res;
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public <T> T getMapper(Class<T> type) {
        return configuration.getMapper(type, this);
    }
}
