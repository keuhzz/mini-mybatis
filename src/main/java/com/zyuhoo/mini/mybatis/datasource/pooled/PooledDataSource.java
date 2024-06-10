package com.zyuhoo.mini.mybatis.datasource.pooled;

import com.zyuhoo.mini.mybatis.datasource.unpooled.UnpooledDataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.slf4j.LoggerFactory;

/**
 * About PooledDataSource.
 *
 * @since 0.0.1
 */
public class PooledDataSource implements DataSource {

    private org.slf4j.Logger log = LoggerFactory.getLogger(PooledDataSource.class);

    private final ReentrantLock lock = new ReentrantLock();

    private final Condition condition = lock.newCondition();

    // 池状态
    private final PoolState state = new PoolState(this);

    private final UnpooledDataSource dataSource;

    protected int poolMaximumActiveConnections = 10;

    protected int poolMaximumIdleConnections = 5;

    private int poolMaximumCheckoutTime = 20000;

    private int poolMaximumLocalBadConnectionTolerance = 3;

    protected int poolTimeToWait = 20000;

    protected String poolPingQuery = "NO PING QUERY SET";

    protected boolean poolPingEnable = false;

    protected int poolPingConnectionsNotUsedFor = 0;

    private int expectedConnectionTypeCode;

    public PooledDataSource() {
        this.dataSource = new UnpooledDataSource();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return popConnection(dataSource.getUsername(), dataSource.getPassword()).getProxyConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return popConnection(username, password).getProxyConnection();
    }

    private PooledConnection popConnection(String username, String password) throws SQLException {
        boolean countedWait = false;
        PooledConnection conn = null;
        long t = System.currentTimeMillis();
        int localBadConnectionCount = 0;

        while (conn == null) {
            lock.lock();
            try {
                if (!state.idleConnections.isEmpty()) {
                    // 有空闲连接
                    conn = state.idleConnections.remove(0);
                    log.info("Checked out connection ");
                } else if (state.activeConnections.size() < poolMaximumActiveConnections) {
                    // 无空闲连接但活跃连接数小于最大活跃连接数限制, 可以创建新连接
                    conn = new PooledConnection(dataSource.getConnection(), this);
                    log.info("Created connection ");
                } else {
                    // 活跃连接数已满, 无法创建新连接
                    // 获取最老的活跃连接, 看是否过期
                    PooledConnection oldestActiveConnection = state.activeConnections.get(0);
                    long longestCheckoutTime = oldestActiveConnection.getCheckoutTime();
                    // 如果过期, 则删除老连接, 新建一个连接
                    if (longestCheckoutTime > poolMaximumCheckoutTime) {
                        state.claimedOverdueConnectionCount++;
                        state.accumulatedCheckoutTimeOfOverdueConnections += longestCheckoutTime;
                        state.accumulatedCheckoutTime += longestCheckoutTime;
                        state.activeConnections.remove(oldestActiveConnection);
                        if (!oldestActiveConnection.getRealConnection().getAutoCommit()) {
                            oldestActiveConnection.getRealConnection().rollback();
                        }
                        conn = new PooledConnection(dataSource.getConnection(), this);
                        oldestActiveConnection.invalidate();
                        log.info("Claimed overdue connection ");
                    } else {
                        // 如果未过期, 则等待
                        try {
                            if (!countedWait) {
                                state.hadToWaitCount++;
                                countedWait = true;
                            }
                            log.info("Waiting as long as " + poolTimeToWait + " ms for connection.");
                            long wt = System.currentTimeMillis();
                            if (!condition.await(poolTimeToWait, TimeUnit.MILLISECONDS)) {
                                log.info("Wait failed...");
                            }
                            state.accumulatedWaitTime += System.currentTimeMillis() - wt;
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                }

                // 连接是有有效性的, 不是获取到连接就能用, 可能存在网络中断等各种原因导致连接不可用
                if (conn != null) {
                    if (conn.isValid()) {
                        if (!conn.getRealConnection().getAutoCommit()) {
                            // TODO 再次回滚(不理解再次回滚的原因, 根据上面获取连接的过程 conn 已经是可以直接使用的连接)
                            conn.getRealConnection().rollback();
                        }
                        conn.setConnectionTypeCode(assembleConnectionTypeCode(dataSource.getUrl(), username, password));
                        conn.setCheckoutTimestamp(System.currentTimeMillis());
                        conn.setLastUsedTimestamp(System.currentTimeMillis());
                        state.activeConnections.add(conn);
                        state.requestCount++;
                        state.accumulatedRequestTime += System.currentTimeMillis() - t;
                    } else {
                        log.info("A bad connection (" + conn.getRealHashCode()
                            + ") was returned from the pool, getting another connection.");
                        state.badConnectionCount++;
                        localBadConnectionCount++;
                        conn = null;
                        if (localBadConnectionCount > (poolMaximumIdleConnections
                            + poolMaximumLocalBadConnectionTolerance)) {
                            log.info("PooledDataSource: Could not get a good connection to the database");
                            throw new SQLException("PooledDataSource: Could not get a good connection to database.");
                        }
                    }
                }
            } finally {
                lock.unlock();
            }
        }
        return conn;
    }

    protected void pushConnection(PooledConnection connection) throws SQLException {
        lock.lock();
        try {
            state.activeConnections.remove(connection);
            if (connection.isValid()) {
                if (state.idleConnections.size() < poolMaximumIdleConnections
                    && connection.getConnectionTypeCode() == expectedConnectionTypeCode) {
                    state.accumulatedCheckoutTime += connection.getCheckoutTime();
                    if (!connection.getRealConnection().getAutoCommit()) {
                        connection.getRealConnection().rollback();
                    }
                    // Q: 为什么要在真实的连接基础上再创建一个代理连接放进空闲连接队列, 而不是直接将原代理连接放进空闲连接队列
                    // A: 此时使用方可能依旧持有原代理连接, 并且可能(不合规)继续使用. 所以需要重新新建代理连接, 并将原代理连接失效
                    PooledConnection newConnection = new PooledConnection(connection.getRealConnection(), this);
                    state.idleConnections.add(newConnection);
                    newConnection.setCreatedTimestamp(connection.getCreatedTimestamp());
                    newConnection.setLastUsedTimestamp(connection.getLastUsedTimestamp());
                    connection.invalidate();
                    log.info("Returned connection " + newConnection.getRealHashCode() + " to pool.");
                    // 通知其他线程可以抢占空闲连接
                    condition.signal();
                } else {
                    state.accumulatedCheckoutTime += connection.getCheckoutTime();
                    if (!connection.getRealConnection().getAutoCommit()) {
                        connection.getRealConnection().rollback();
                    }
                    connection.getRealConnection().close();
                    log.info("Closed connection " + connection.getRealHashCode() + ".");
                    connection.invalidate();
                }
            } else {
                log.info("A bad connection (" + connection.getRealHashCode()
                    + ") attempted to return to the pool, discarding connection.");
                state.badConnectionCount++;
            }

        } finally {
            lock.unlock();
        }

    }

    public void forceCloseAll() {
        lock.lock();
        try {
            expectedConnectionTypeCode =
                assembleConnectionTypeCode(dataSource.getUrl(), dataSource.getUsername(), dataSource.getPassword());
            // 关闭活跃连接
            for (int i = state.activeConnections.size(); i > 0; i--) {
                try {
                    PooledConnection conn = state.activeConnections.remove(i - 1);
                    conn.invalidate();

                    Connection realConn = conn.getRealConnection();
                    if (!realConn.getAutoCommit()) {
                        realConn.rollback();
                    }
                    realConn.close();
                } catch (Exception e) {
                    // ignore
                }
            }
            // 关闭空闲连接
            for (int i = state.idleConnections.size(); i > 0; i--) {
                try {
                    PooledConnection conn = state.idleConnections.remove(i - 1);
                    conn.invalidate();

                    Connection realConn = conn.getRealConnection();
                    if (!realConn.getAutoCommit()) {
                        realConn.rollback();
                    }
                    realConn.close();
                } catch (Exception e) {
                    // ignore
                }
            }
        } finally {
            lock.unlock();
        }
        log.info("PooledDataSource forcefully closed/removed all connections.");
    }

    private int assembleConnectionTypeCode(String url, String username, String password) {
        return (url + username + password).hashCode();
    }

    protected boolean pingConnection(PooledConnection conn) {
        // TODO 实现连接探针
        return true;
    }

    @Override
    protected void finalize() throws Throwable {
        forceCloseAll();
        super.finalize();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException(getClass().getName() + " is not a wrapper.");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return DriverManager.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        DriverManager.setLogWriter(out);

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        DriverManager.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return DriverManager.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }

    public void setDriver(String driver) {
        dataSource.setDriver(driver);
        forceCloseAll();
    }

    public void setUrl(String url) {
        dataSource.setUrl(url);
        forceCloseAll();
    }

    public void setUsername(String username) {
        dataSource.setUsername(username);
        forceCloseAll();
    }

    public void setPassword(String password) {
        dataSource.setPassword(password);
        forceCloseAll();
    }

    public int getPoolMaximumActiveConnections() {
        return poolMaximumActiveConnections;
    }

    public void setPoolMaximumActiveConnections(int poolMaximumActiveConnections) {
        this.poolMaximumActiveConnections = poolMaximumActiveConnections;
    }

    public int getPoolMaximumIdleConnections() {
        return poolMaximumIdleConnections;
    }

    public void setPoolMaximumIdleConnections(int poolMaximumIdleConnections) {
        this.poolMaximumIdleConnections = poolMaximumIdleConnections;
    }

    public int getPoolMaximumCheckoutTime() {
        return poolMaximumCheckoutTime;
    }

    public void setPoolMaximumCheckoutTime(int poolMaximumCheckoutTime) {
        this.poolMaximumCheckoutTime = poolMaximumCheckoutTime;
    }

    public int getPoolMaximumLocalBadConnectionTolerance() {
        return poolMaximumLocalBadConnectionTolerance;
    }

    public void setPoolMaximumLocalBadConnectionTolerance(int poolMaximumLocalBadConnectionTolerance) {
        this.poolMaximumLocalBadConnectionTolerance = poolMaximumLocalBadConnectionTolerance;
    }

    public int getPoolTimeToWait() {
        return poolTimeToWait;
    }

    public void setPoolTimeToWait(int poolTimeToWait) {
        this.poolTimeToWait = poolTimeToWait;
    }

    public String getPoolPingQuery() {
        return poolPingQuery;
    }

    public void setPoolPingQuery(String poolPingQuery) {
        this.poolPingQuery = poolPingQuery;
    }

    public boolean isPoolPingEnable() {
        return poolPingEnable;
    }

    public void setPoolPingEnable(boolean poolPingEnable) {
        this.poolPingEnable = poolPingEnable;
    }

    public int getPoolPingConnectionsNotUsedFor() {
        return poolPingConnectionsNotUsedFor;
    }

    public void setPoolPingConnectionsNotUsedFor(int poolPingConnectionsNotUsedFor) {
        this.poolPingConnectionsNotUsedFor = poolPingConnectionsNotUsedFor;
    }

    public int getExpectedConnectionTypeCode() {
        return expectedConnectionTypeCode;
    }

    public void setExpectedConnectionTypeCode(int expectedConnectionTypeCode) {
        this.expectedConnectionTypeCode = expectedConnectionTypeCode;
    }
}
