package com.zyuhoo.mini.mybatis.datasource.pooled;

import com.zyuhoo.mini.mybatis.datasource.unpooled.UnpooledDataSource;
import com.zyuhoo.mini.mybatis.datasource.unpooled.UnpooledDataSourceFactory;
import javax.sql.DataSource;

/**
 * About PooledDataSourceFactory.
 *
 * @since 0.0.1
 */
public class PooledDataSourceFactory extends UnpooledDataSourceFactory {

    @Override
    public DataSource getDataSource() {
        PooledDataSource pooledDataSource = new PooledDataSource();
        pooledDataSource.setDriver(props.getProperty("driver"));
        pooledDataSource.setUrl(System.getProperty(props.getProperty("url")));
        pooledDataSource.setUsername(System.getProperty(props.getProperty("username")));
        pooledDataSource.setPassword(System.getProperty(props.getProperty("password")));
        return pooledDataSource;
    }


}
