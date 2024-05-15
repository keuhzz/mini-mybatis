package com.zyuhoo.mini.mybatis.datasource.pooled;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * About PooledConnection. 增加时间统计功能
 *
 * @since 0.0.1
 */
public class PooledConnection implements InvocationHandler {

    private static final Class<?>[] IFACES = {Connection.class};
    private static final String CLOSE = "close";

    // 重写 hashCode 和 equals 方法
    private int hashCode = 0;

    private PooledDataSource dataSource;

    private Connection realConnection;

    private Connection proxyConnection;

    // 签出时间戳: 从连接池获取连接的时间戳
    private long checkoutTimestamp;

    // 创建时间戳
    private long createdTimestamp;

    // 最近使用时间戳
    private long lastUsedTimestamp;

    // 区分连接类型
    private int connectionTypeCode;

    private boolean valid;

    public PooledConnection(Connection realConnection, PooledDataSource dataSource) {
        this.hashCode = realConnection.hashCode();
        this.realConnection = realConnection;
        this.dataSource = dataSource;
        this.createdTimestamp = System.currentTimeMillis();
        this.lastUsedTimestamp = System.currentTimeMillis();
        this.valid = true;
        this.proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), IFACES, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (CLOSE.equals(method.getName())) {
            dataSource.pushConnection(this);
            return null;
        }
        if (!Object.class.equals(method.getDeclaringClass())) {
            checkConnection();
        }
        return method.invoke(realConnection, args);
    }

    public void checkConnection() throws SQLException {
        if (!valid) {
            throw new SQLException("Error accessing PooledConnection. Connection is invalid.");
        }
    }

    public void invalidate() {
        this.valid = false;
    }

    public boolean isValid() {
        return valid;
    }

    public Connection getRealConnection() {
        return realConnection;
    }

    public int getRealHashCode() {
        return realConnection == null ? 0 : realConnection.hashCode();
    }

    public Connection getProxyConnection() {
        return proxyConnection;
    }

    public int getConnectionTypeCode() {
        return connectionTypeCode;
    }

    public void setConnectionTypeCode(int connectionTypeCode) {
        this.connectionTypeCode = connectionTypeCode;
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public long getLastUsedTimestamp() {
        return lastUsedTimestamp;
    }

    public void setLastUsedTimestamp(long lastUsedTimestamp) {
        this.lastUsedTimestamp = lastUsedTimestamp;
    }

    public long getTimeElapsedSinceLastUse() {
        return System.currentTimeMillis() - lastUsedTimestamp;
    }

    public long getAge() {
        return System.currentTimeMillis() - createdTimestamp;
    }

    public long getCheckoutTimestamp() {
        return checkoutTimestamp;
    }

    public void setCheckoutTimestamp(long checkoutTimestamp) {
        this.checkoutTimestamp = checkoutTimestamp;
    }

    public long getCheckoutTime() {
        return System.currentTimeMillis() - checkoutTimestamp;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PooledConnection) {
            return realConnection.hashCode() == ((PooledConnection) obj).realConnection.hashCode();
        }
        if (obj instanceof Connection) {
            return hashCode == obj.hashCode();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
