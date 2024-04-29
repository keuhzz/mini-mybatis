package com.zyuhoo.mini.mybatis.session;

import com.zyuhoo.mini.mybatis.builder.xml.XmlConfigBuilder;
import com.zyuhoo.mini.mybatis.session.defaults.DefaultSqlSessionFactory;
import java.io.Reader;

/**
 * About SqlSessionFactoryBuilder.
 *
 * @since 0.0.1
 */
public class SqlSessionFactoryBuilder {

    public SqlSessionFactory build(Reader reader) {
        XmlConfigBuilder xmlConfigBuilder = new XmlConfigBuilder(reader);
        return new DefaultSqlSessionFactory(xmlConfigBuilder.parse());
    }

}