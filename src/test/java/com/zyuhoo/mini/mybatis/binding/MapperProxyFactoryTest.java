package com.zyuhoo.mini.mybatis.binding;

import com.zyuhoo.mini.mybatis.dao.UserDao;
import com.zyuhoo.mini.mybatis.io.Resources;
import com.zyuhoo.mini.mybatis.session.Configuration;
import com.zyuhoo.mini.mybatis.session.SqlSession;
import com.zyuhoo.mini.mybatis.session.SqlSessionFactory;
import com.zyuhoo.mini.mybatis.session.SqlSessionFactoryBuilder;
import com.zyuhoo.mini.mybatis.session.defaults.DefaultSqlSession;
import java.io.IOException;
import java.io.Reader;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapperProxyFactoryTest {
    private static final Logger log = LoggerFactory.getLogger(MapperProxyFactoryTest.class);

    @Test
    public void newInstance() {
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader("./datasource-config.xml");
        } catch (IOException e) {
            log.info("load datasource config fail", e);
        }
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        String s = userDao.queryUserName("10001");
        log.info("the res: {}", s);
        Assert.assertTrue(s != null && s.startsWith("execute select sql"));
    }
}