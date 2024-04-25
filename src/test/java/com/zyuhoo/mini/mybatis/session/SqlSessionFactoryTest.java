package com.zyuhoo.mini.mybatis.session;

import com.zyuhoo.mini.mybatis.binding.MapperRegistry;
import com.zyuhoo.mini.mybatis.dao.UserDao;
import com.zyuhoo.mini.mybatis.session.defaults.DefaultSqlSessionFactory;
import org.junit.Assert;
import org.junit.Test;

public class SqlSessionFactoryTest {

    @Test
    public void openSession() {
        Configuration configuration = new Configuration();
        String packageName = "com.zyuhoo.mini.mybatis.dao";
        configuration.addMappers(packageName);

        SqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        SqlSession sqlSession = sqlSessionFactory.openSession();

        UserDao userDao = sqlSession.getMapper(UserDao.class);

        String s = userDao.queryUserName("10234");
        Assert.assertTrue(s != null && s.startsWith("execute proxy method invoke"));
    }
}