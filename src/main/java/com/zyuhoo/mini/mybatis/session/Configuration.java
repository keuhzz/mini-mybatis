package com.zyuhoo.mini.mybatis.session;

import com.zyuhoo.mini.mybatis.binding.MapperRegistry;
import com.zyuhoo.mini.mybatis.datasource.druid.DruidDataSourceFactory;
import com.zyuhoo.mini.mybatis.datasource.pooled.PooledDataSourceFactory;
import com.zyuhoo.mini.mybatis.datasource.unpooled.UnpooledDataSourceFactory;
import com.zyuhoo.mini.mybatis.executor.Executor;
import com.zyuhoo.mini.mybatis.executor.SimpleExecutor;
import com.zyuhoo.mini.mybatis.executor.resultset.DefaultResultSetHandler;
import com.zyuhoo.mini.mybatis.executor.resultset.ResultSetHandler;
import com.zyuhoo.mini.mybatis.executor.statement.PreparedStatementHandler;
import com.zyuhoo.mini.mybatis.executor.statement.StatementHandler;
import com.zyuhoo.mini.mybatis.mapping.BoundSql;
import com.zyuhoo.mini.mybatis.mapping.Environment;
import com.zyuhoo.mini.mybatis.mapping.MappedStatement;
import com.zyuhoo.mini.mybatis.transaction.Transaction;
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
        typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);
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

    /**
     * 创建结果集处理器.
     */
    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, BoundSql boundSql) {
        return new DefaultResultSetHandler(executor, mappedStatement, boundSql);
    }

    /**
     * 创建执行器.
     */
    public Executor newExecutor(Transaction transaction) {
        return new SimpleExecutor(this, transaction);
    }

    /**
     * 创建语句处理器.
     */
    public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameter,
        BoundSql boundSql) {
        return new PreparedStatementHandler(executor, mappedStatement, parameter, boundSql);
    }
}
