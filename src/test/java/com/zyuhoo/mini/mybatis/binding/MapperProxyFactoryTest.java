package com.zyuhoo.mini.mybatis.binding;

import com.zyuhoo.mini.mybatis.dao.UserDao;
import com.zyuhoo.mini.mybatis.session.SqlSession;
import com.zyuhoo.mini.mybatis.session.defaults.DefaultSqlSession;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapperProxyFactoryTest {
    private static final Logger log = LoggerFactory.getLogger(MapperProxyFactoryTest.class);

    @Test
    public void newInstance() {
        MapperRegistry mapperRegistry = new MapperRegistry();
        String packageName = "com.zyuhoo.mini.mybatis.dao";
        mapperRegistry.addMappers(packageName);

        SqlSession sqlSession = new DefaultSqlSession(mapperRegistry);

        MapperProxyFactory<UserDao> mapperProxyFactory = new MapperProxyFactory<>(UserDao.class);
        UserDao userDao = mapperProxyFactory.newInstance(sqlSession);
        String s = userDao.queryUserName("10001");
        log.info("the res: {}", s);
        Assert.assertTrue(s != null && s.startsWith("execute proxy method invoke"));
    }
}