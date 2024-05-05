package com.zyuhoo.mini.mybatis.builder;

import com.zyuhoo.mini.mybatis.session.Configuration;
import com.zyuhoo.mini.mybatis.type.TypeAliasRegistry;

/**
 * About BaseBuilder.
 *
 * @since 0.0.1
 */
public class BaseBuilder {
    protected final Configuration configuration;

    protected final TypeAliasRegistry typeAliasRegistry;

    public BaseBuilder(Configuration configuration) {
        this.configuration = configuration;
        this.typeAliasRegistry = this.configuration.getTypeAliasRegistry();
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
