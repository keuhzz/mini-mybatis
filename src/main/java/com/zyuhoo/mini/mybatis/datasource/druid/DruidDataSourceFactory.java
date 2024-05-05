package com.zyuhoo.mini.mybatis.datasource.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.zyuhoo.mini.mybatis.datasource.DataSourceFactory;
import java.util.Properties;
import javax.sql.DataSource;

/**
 * About DruidDataSourceFactory.
 *
 * @since 0.0.1
 */
public class DruidDataSourceFactory implements DataSourceFactory {

    private Properties props;

    @Override
    public void setProperties(Properties props) {
        this.props = props;
    }

    @Override
    public DataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(props.getProperty("driver"));
        dataSource.setUrl(System.getProperty(props.getProperty("url")));
        dataSource.setUsername(System.getProperty(props.getProperty("username")));
        dataSource.setPassword(System.getProperty(props.getProperty("password")));
        return dataSource;
    }
}
