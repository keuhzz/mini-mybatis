package com.zyuhoo.mini.mybatis.session.defaults;

import com.zyuhoo.mini.mybatis.session.Configuration;
import com.zyuhoo.mini.mybatis.session.SqlSession;
import com.zyuhoo.mini.mybatis.session.SqlSessionFactory;

/**
 * SqlSession 工厂默认实现.
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private final Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
