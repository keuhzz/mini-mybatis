package com.zyuhoo.mini.mybatis.transaction.jdbc;

import com.zyuhoo.mini.mybatis.session.TransactionIsolationLevel;
import com.zyuhoo.mini.mybatis.transaction.Transaction;
import com.zyuhoo.mini.mybatis.transaction.TransactionFactory;
import java.sql.Connection;
import javax.sql.DataSource;

/**
 * About JdbcTransactionFactory.
 *
 * @since 0.0.1
 */
public class JdbcTransactionFactory implements TransactionFactory {

    @Override
    public Transaction newTransaction(Connection connection) {
        return new JdbcTransaction(connection);
    }

    @Override
    public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        return new JdbcTransaction(dataSource, level, autoCommit);
    }
}
