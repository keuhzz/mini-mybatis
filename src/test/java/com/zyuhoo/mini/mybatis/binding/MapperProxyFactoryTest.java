package com.zyuhoo.mini.mybatis.binding;

import com.zyuhoo.mini.mybatis.dao.UserDao;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapperProxyFactoryTest {
    private static final Logger log = LoggerFactory.getLogger(MapperProxyFactoryTest.class);

    @Test
    public void newInstance() {
        MapperProxyFactory<UserDao> mapperProxyFactory = new MapperProxyFactory<>(UserDao.class);
        Map<String, String> sqlSession = new HashMap<>();
        sqlSession.put("com.zyuhoo.mini.mybatis.dao.UserDao.queryUserName", "sql for queryUserName");
        sqlSession.put("com.zyuhoo.mini.mybatis.dao.UserDao.queryUserAge", "sql for queryUserAge");

        UserDao userDao = mapperProxyFactory.newInstance(sqlSession);
        String s = userDao.queryUserName("10001");
        log.info("the res: {}", s);
        Assert.assertTrue(s != null && s.startsWith("execute proxy method invoke"));
    }
}