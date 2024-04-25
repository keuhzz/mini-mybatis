package com.zyuhoo.mini.mybatis.builder;

import com.zyuhoo.mini.mybatis.session.Configuration;

/**
 * About BaseBuilder.
 *
 * @since 0.0.1
 */
public class BaseBuilder {
    protected final Configuration configuration;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
