package com.zyuhoo.mini.mybatis.session;

/**
 * SqlSession 工厂.
 */
public interface SqlSessionFactory {
    SqlSession openSession();
}
