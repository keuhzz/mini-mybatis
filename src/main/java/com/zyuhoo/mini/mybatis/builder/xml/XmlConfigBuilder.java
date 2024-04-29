package com.zyuhoo.mini.mybatis.builder.xml;

import com.zyuhoo.mini.mybatis.builder.BaseBuilder;
import com.zyuhoo.mini.mybatis.io.Resources;
import com.zyuhoo.mini.mybatis.mapping.MappedStatement;
import com.zyuhoo.mini.mybatis.mapping.MappedStatement.Builder;
import com.zyuhoo.mini.mybatis.mapping.SqlCommandType;
import com.zyuhoo.mini.mybatis.session.Configuration;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

/**
 * About XmlConfigBuilder.
 *
 * @since 0.0.1
 */
public class XmlConfigBuilder extends BaseBuilder {

    private static final Logger log = LoggerFactory.getLogger(XmlConfigBuilder.class);
    private Element root;

    public XmlConfigBuilder(Reader reader) {
        super(new Configuration());
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(new InputSource(reader));
            root = document.getRootElement();
        } catch (DocumentException e) {
            log.error("constructor fail", e);
        }
    }

    public Configuration parse() {
        Element mappers = root.element("mappers");
        List<Element> mapperList = mappers.elements("mapper");
        try {
            mapperElement(mapperList);
        } catch (IOException | DocumentException | ClassNotFoundException e) {
            throw new RuntimeException("Error parsing SQL Mapper Configuration. Cause: " + e, e);
        }
        return configuration;
    }

    private void mapperElement(List<Element> mapperList) throws IOException, DocumentException, ClassNotFoundException {
        for (Element mapper : mapperList) {
            String resource = mapper.attributeValue("resource");
            Reader reader = Resources.getResourceAsReader(resource);
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(new InputSource(reader));
            Element mapperRoot = document.getRootElement();
            String namespace = mapperRoot.attributeValue("namespace");

            List<Element> selectNodes = mapperRoot.elements("select");
            for (Element node : selectNodes) {
                String id = node.attributeValue("id");
                String parameterType = node.attributeValue("parameterType");
                String resultType = node.attributeValue("resultType");
                String sql = node.getText();
                // 参数与占位符 #{}
                Map<Integer, String> parameter = new HashMap<>();
                Pattern pattern = Pattern.compile("(#\\{(.*?)})");
                Matcher matcher = pattern.matcher(sql);
                for (int i = 0; matcher.find(); i++) {
                    String placeHolder = matcher.group(1);
                    String paramName = matcher.group(2);
                    parameter.put(i, paramName);
                    sql = sql.replace(placeHolder, "?");
                }

                String msId = namespace + "." + id;
                String nodeName = node.getName();
                SqlCommandType sqlCommandType = SqlCommandType.valueOf(nodeName.toUpperCase(Locale.ROOT));
                MappedStatement mappedStatement =
                    new Builder(msId, sqlCommandType, parameterType, resultType, sql, parameter).build();
                // 添加 SQL 解析
                configuration.addMappedStatement(mappedStatement);
            }
            // 注册 Mapper 映射器
            configuration.addMapper(Resources.classForName(namespace));
        }
    }

}
