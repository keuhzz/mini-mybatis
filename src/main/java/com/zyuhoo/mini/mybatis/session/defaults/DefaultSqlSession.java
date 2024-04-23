package com.zyuhoo.mini.mybatis.session.defaults;

import com.zyuhoo.mini.mybatis.binding.MapperRegistry;
import com.zyuhoo.mini.mybatis.session.SqlSession;

/**
 * SqlSession 默认实现.
 */
public class DefaultSqlSession implements SqlSession {
    private final MapperRegistry mapperRegistry;

    public DefaultSqlSession(MapperRegistry mapperRegistry) {
        this.mapperRegistry = mapperRegistry;
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
    public <T> T getMapper(Class<T> type) {
        return mapperRegistry.getMapper(type, this);
    }
}
