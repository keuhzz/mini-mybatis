package com.zyuhoo.mini.mybatis.mapping;

import java.util.Map;

/**
 * About MappedStatement.
 *
 * @since 0.0.1
 */
public class MappedStatement {
    private String id;

    private String parameterType;

    private String resultType;

    private SqlCommandType sqlCommandType;

    private String sql;

    private Map<Integer, String> parameter;

    MappedStatement() {}

    // 建造者模式

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParameterType() {
        return parameterType;
    }

    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public void setSqlCommandType(SqlCommandType sqlCommandType) {
        this.sqlCommandType = sqlCommandType;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Map<Integer, String> getParameter() {
        return parameter;
    }

    public void setParameter(Map<Integer, String> parameter) {
        this.parameter = parameter;
    }
}
