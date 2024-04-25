package com.zyuhoo.mini.mybatis.session.defaults;

import com.zyuhoo.mini.mybatis.session.Configuration;
import com.zyuhoo.mini.mybatis.session.SqlSession;

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
        return (T) ("execute select sql, method: " + statement + " parameter: " + parameter);
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
