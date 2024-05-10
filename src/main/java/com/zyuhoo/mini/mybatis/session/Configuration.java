package com.zyuhoo.mini.mybatis.session;

import com.zyuhoo.mini.mybatis.binding.MapperRegistry;
import com.zyuhoo.mini.mybatis.datasource.druid.DruidDataSourceFactory;
import com.zyuhoo.mini.mybatis.datasource.unpooled.UnpooledDataSourceFactory;
import com.zyuhoo.mini.mybatis.mapping.Environment;
import com.zyuhoo.mini.mybatis.mapping.MappedStatement;
import com.zyuhoo.mini.mybatis.transaction.jdbc.JdbcTransactionFactory;
import com.zyuhoo.mini.mybatis.type.TypeAliasRegistry;
import java.util.HashMap;
import java.util.Map;

/**
 * About Configuration.
 *
 * @since 0.0.1
 */
public class Configuration {

    protected Environment environment;

    protected MapperRegistry mapperRegistry = new MapperRegistry();

    // 全局 config

    // mapper xml: Dao interface <-> SQL
    protected final Map<String, MappedStatement> mappedStatements = new HashMap<>();

    protected final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    public Configuration() {
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);
        typeAliasRegistry.registerAlias("DRUID", DruidDataSourceFactory.class);
        typeAliasRegistry.registerAlias("UNPOOLED", UnpooledDataSourceFactory.class);
    }


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

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
