package com.zyuhoo.mini.mybatis.datasource.unpooled;

import com.zyuhoo.mini.mybatis.datasource.DataSourceFactory;
import java.util.Properties;
import javax.sql.DataSource;

/**
 * About UnpooledDataSourceFactory.
 *
 * @since 0.0.1
 */
public class UnpooledDataSourceFactory implements DataSourceFactory {
    protected Properties props;

    @Override
    public void setProperties(Properties props) {
        this.props = props;
    }

    @Override
    public DataSource getDataSource() {
        UnpooledDataSource unpooledDataSource = new UnpooledDataSource();
        unpooledDataSource.setDriver(props.getProperty("driver"));
        unpooledDataSource.setUrl(System.getProperty(props.getProperty("url")));
        unpooledDataSource.setUsername(System.getProperty(props.getProperty("username")));
        unpooledDataSource.setPassword(System.getProperty(props.getProperty("password")));
        return unpooledDataSource;
    }
}
