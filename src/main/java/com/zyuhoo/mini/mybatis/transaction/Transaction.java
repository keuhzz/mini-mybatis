package com.zyuhoo.mini.mybatis.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * About Transaction.
 *
 * @since 0.0.1
 */
public interface Transaction {

    Connection getConnection() throws SQLException;

    void commit() throws SQLException;

    void rollback() throws SQLException;

    void close() throws SQLException;
}
