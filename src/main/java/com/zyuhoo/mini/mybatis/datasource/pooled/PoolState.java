package com.zyuhoo.mini.mybatis.datasource.pooled;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * About PoolState.
 *
 * @since 0.0.1
 */
public class PoolState {

    private final ReentrantLock lock = new ReentrantLock();

    protected PooledDataSource dataSource;

    // 空闲连接
    protected final List<PooledConnection> idleConnections = new ArrayList<>();

    // 活跃连接
    protected final List<PooledConnection> activeConnections = new ArrayList<>();

    // 请求次数
    protected long requestCount = 0L;

    // 累计请求时间
    protected long accumulatedRequestTime = 0L;

    // 累计签出时间
    protected long accumulatedCheckoutTime = 0L;

    // 已声明过期连接数
    protected long claimedOverdueConnectionCount = 0L;

    // 过期连接的累计签出时间
    protected long accumulatedCheckoutTimeOfOverdueConnections = 0L;

    // 累计等待时间
    protected long accumulatedWaitTime = 0L;

    // 必须等待次数
    protected long hadToWaitCount = 0L;

    // 坏连接个数
    protected long badConnectionCount = 0L;

    public PoolState(PooledDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public long getRequestCount() {
        lock.lock();
        try {
            return requestCount;
        } finally {
            lock.unlock();
        }
    }

    public long getHadToWaitCount() {
        lock.lock();
        try {
            return hadToWaitCount;
        } finally {
            lock.unlock();
        }
    }

    public long getBadConnectionCount() {
        lock.lock();
        try {
            return badConnectionCount;
        } finally {
            lock.unlock();
        }
    }

    public long getClaimedOverdueConnectionCount() {
        lock.lock();
        try {
            return claimedOverdueConnectionCount;
        } finally {
            lock.unlock();
        }
    }
}