package com.zyuhoo.mini.mybatis.session;

/**
 * 抽象与数据库的交互, 如增删改查, 数据库连接, 事务管理.
 */
public interface SqlSession {
    <T> T selectOne(String statement);

    <T> T selectOne(String statement, Object parameter);

    <T> T getMapper(Class<T> type);
}
