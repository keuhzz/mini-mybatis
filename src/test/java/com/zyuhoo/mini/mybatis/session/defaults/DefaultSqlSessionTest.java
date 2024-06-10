package com.zyuhoo.mini.mybatis.session.defaults;

import com.zyuhoo.mini.mybatis.builder.xml.XmlConfigBuilder;
import com.zyuhoo.mini.mybatis.entity.User;
import com.zyuhoo.mini.mybatis.executor.SimpleExecutor;
import com.zyuhoo.mini.mybatis.io.Resources;
import com.zyuhoo.mini.mybatis.session.Configuration;
import com.zyuhoo.mini.mybatis.session.SqlSession;
import com.zyuhoo.mini.mybatis.session.TransactionIsolationLevel;
import com.zyuhoo.mini.mybatis.transaction.Transaction;
import com.zyuhoo.mini.mybatis.transaction.TransactionFactory;
import com.zyuhoo.mini.mybatis.transaction.jdbc.JdbcTransactionFactory;
import java.io.IOException;
import java.io.Reader;
import org.junit.Assert;
import org.junit.Test;

public class DefaultSqlSessionTest {

    @Test
    public void selectOne() throws IOException {
        Reader reader = Resources.getResourceAsReader("datasource-config.xml");
        XmlConfigBuilder xmlConfigBuilder = new XmlConfigBuilder(reader);
        Configuration configuration = xmlConfigBuilder.parse();
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Transaction transaction = transactionFactory.newTransaction(configuration.getEnvironment().getDataSource(),
            TransactionIsolationLevel.READ_COMMITTED, false);
        SqlSession sqlSession = new DefaultSqlSession(configuration, new SimpleExecutor(configuration, transaction));
        String msId = "com.zyuhoo.mini.mybatis.dao.UserDao.queryUserInfoById";
        Object[] param = new Object[] {1L};
        User res = sqlSession.selectOne(msId, param);
        Assert.assertTrue(res != null && res.getId() == 1L);
    }
}