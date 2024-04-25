package com.zyuhoo.mini.mybatis.session;

import com.zyuhoo.mini.mybatis.binding.MapperRegistry;
import com.zyuhoo.mini.mybatis.mapping.MappedStatement;
import java.util.HashMap;
import java.util.Map;

/**
 * About Configuration.
 *
 * @since 0.0.1
 */
public class Configuration {

    protected MapperRegistry mapperRegistry = new MapperRegistry();

    // 全局 config

    // mapper xml: Dao interface <-> SQL
    protected final Map<String, MappedStatement> mappedStatements = new HashMap<>();


    public void addMappers(String packageName) {
        mapperRegistry.addMappers(packageName);
    }

    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    public void addMappedStatement(MappedStatement ms) {
        mappedStatements.put(ms.getId(), ms);
    }

    public MappedStatement getMappedStatement(String id) {
        return mappedStatements.get(id);
    }
}
