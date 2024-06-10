package com.zyuhoo.mini.mybatis.session.defaults;

import com.zyuhoo.mini.mybatis.executor.Executor;
import com.zyuhoo.mini.mybatis.mapping.Environment;
import com.zyuhoo.mini.mybatis.session.Configuration;
import com.zyuhoo.mini.mybatis.session.SqlSession;
import com.zyuhoo.mini.mybatis.session.SqlSessionFactory;
import com.zyuhoo.mini.mybatis.session.TransactionIsolationLevel;
import com.zyuhoo.mini.mybatis.transaction.Transaction;
import com.zyuhoo.mini.mybatis.transaction.TransactionFactory;
import java.sql.SQLException;

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
        Transaction tx = null;
        try {
            Environment environment = configuration.getEnvironment();
            TransactionFactory transactionFactory = environment.getTransactionFactory();
            tx = transactionFactory.newTransaction(environment.getDataSource(), TransactionIsolationLevel.READ_COMMITTED, false);
            Executor executor = configuration.newExecutor(tx);
            return new DefaultSqlSession(configuration, executor);
        } catch (Exception e) {
            if (tx != null) {
                try {
                    tx.close();
                } catch (SQLException ignore) {
                    // nothing
                }
            }
            throw new RuntimeException("Error opening session. Cause: ", e);
        }
    }
}
