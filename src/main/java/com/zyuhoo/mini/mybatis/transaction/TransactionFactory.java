package com.zyuhoo.mini.mybatis.transaction;

import com.zyuhoo.mini.mybatis.session.TransactionIsolationLevel;
import java.sql.Connection;
import javax.sql.DataSource;

/**
 * About TransactionFactory.
 *
 * @since 0.0.1
 */
public interface TransactionFactory {

    Transaction newTransaction(Connection connection);

    Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit);
}
