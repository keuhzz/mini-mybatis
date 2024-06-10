package com.zyuhoo.mini.mybatis.executor.resultset;

import com.zyuhoo.mini.mybatis.executor.Executor;
import com.zyuhoo.mini.mybatis.mapping.BoundSql;
import com.zyuhoo.mini.mybatis.mapping.MappedStatement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * About DefaultResultSetHandler.
 *
 * @since 0.0.1
 */
public class DefaultResultSetHandler implements ResultSetHandler {

    private static final Logger log = LoggerFactory.getLogger(DefaultResultSetHandler.class);

    private final Executor executor;
    private final MappedStatement mappedStatement;
    private final BoundSql boundSql;

    public DefaultResultSetHandler(Executor executor, MappedStatement mappedStatement, BoundSql boundSql) {
        this.executor = executor;
        this.mappedStatement = mappedStatement;
        this.boundSql = boundSql;
    }

    @Override
    public <E> List<E> handleResultSets(Statement stmt) throws SQLException {
        // TODO 结果集处理
        ResultSet resultSet = stmt.getResultSet();
        try {
            return resultSet2Obj(resultSet, Class.forName(boundSql.getResultType()));
        } catch (ClassNotFoundException e) {
            log.error("Error handle result set. Cause: ", e);
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    // nothing
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <E> List<E> resultSet2Obj(ResultSet resultSet, Class<?> clazz) {
        List<E> list = new ArrayList<>();
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                E row = (E) clazz.newInstance();
                for (int i = 1; i < columnCount; i++) {
                    Object value = resultSet.getObject(i);
                    String columnName = metaData.getColumnName(i);
                    String setMethod =
                        "set" + columnName.substring(0, 1).toUpperCase(Locale.ROOT) + columnName.substring(1);
                    Method method;
                    if (value instanceof Timestamp) {
                        method = clazz.getMethod(setMethod, Date.class);
                    } else {
                        method = clazz.getMethod(setMethod, value.getClass());
                    }
                    method.invoke(row, value);
                }
                list.add(row);
            }
        } catch (SQLException | InstantiationException | IllegalAccessException | InvocationTargetException
            | NoSuchMethodException e) {
            log.error("Error result set to object. Cause: ", e);
        }
        return list;

    }
}
