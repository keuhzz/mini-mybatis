package com.zyuhoo.mini.mybatis.session;

import com.zyuhoo.mini.mybatis.io.Resources;
import com.zyuhoo.mini.mybatis.mapping.MappedStatement;
import java.io.IOException;
import java.io.Reader;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlSessionFactoryTest {

    private static final Logger log = LoggerFactory.getLogger(SqlSessionFactoryTest.class);

    @Test
    public void openSession() {
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader("./datasource-config.xml");
        } catch (IOException e) {
            log.info("load datasource config fail", e);
        }
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Configuration configuration = sqlSession.getConfiguration();
        MappedStatement mappedStatement =
            configuration.getMappedStatement("com.zyuhoo.mini.mybatis.dao.UserDao.queryUserInfoById");
        String sql = mappedStatement.getBoundSql().getSql();
        Assert.assertTrue(sql != null && sql.contains("SELECT"));
    }
}