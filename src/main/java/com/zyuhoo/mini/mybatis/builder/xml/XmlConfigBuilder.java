package com.zyuhoo.mini.mybatis.builder.xml;

import com.zyuhoo.mini.mybatis.builder.BaseBuilder;
import com.zyuhoo.mini.mybatis.session.Configuration;

/**
 * About XmlConfigBuilder.
 *
 * @since 0.0.1
 */
public class XmlConfigBuilder extends BaseBuilder {

    public XmlConfigBuilder() {
        super(new Configuration());
    }

    public Configuration parse() {
        // TODO 处理 XML 文件内容, 填充到 config
        return new Configuration();
    }

}
