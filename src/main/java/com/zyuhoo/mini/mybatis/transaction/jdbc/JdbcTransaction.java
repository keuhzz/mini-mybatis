package com.zyuhoo.mini.mybatis.transaction.jdbc;

import com.zyuhoo.mini.mybatis.session.TransactionIsolationLevel;
import com.zyuhoo.mini.mybatis.transaction.Transaction;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * About JdbcTransaction.
 *
 * @since 0.0.1
 */
public class JdbcTransaction implements Transaction {

    private Connection connection;

    private DataSource dataSource;

    private TransactionIsolationLevel level;

    private boolean autoCommit;

    public JdbcTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
        this.dataSource = dataSource;
        this.level = level;
        this.autoCommit = autoCommit;
    }

    public JdbcTransaction(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = dataSource.getConnection();
            connection.setTransactionIsolation(level.getLevel());
            connection.setAutoCommit(autoCommit);
        }
        return connection;
    }

    @Override
    public void commit() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            connection.commit();
        }
    }

    @Override
    public void rollback() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            connection.rollback();
        }
    }

    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.getAutoCommit()) {
            connection.close();
        }

    }
}
