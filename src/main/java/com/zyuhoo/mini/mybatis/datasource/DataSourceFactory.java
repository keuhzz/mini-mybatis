package com.zyuhoo.mini.mybatis.datasource;

import java.util.Properties;
import javax.sql.DataSource;

/**
 * About DataSourceFactory.
 *
 * @since 0.0.1
 */
public interface DataSourceFactory {

    void setProperties(Properties props);

    DataSource getDataSource();
}
