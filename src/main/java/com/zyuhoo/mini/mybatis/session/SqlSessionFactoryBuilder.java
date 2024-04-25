package com.zyuhoo.mini.mybatis.session;

import com.zyuhoo.mini.mybatis.builder.xml.XmlConfigBuilder;
import com.zyuhoo.mini.mybatis.session.defaults.DefaultSqlSessionFactory;

/**
 * About SqlSessionFactoryBuilder.
 *
 * @since 0.0.1
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build() {
        XmlConfigBuilder xmlConfigBuilder = new XmlConfigBuilder();
        return new DefaultSqlSessionFactory(xmlConfigBuilder.parse());
    }

}