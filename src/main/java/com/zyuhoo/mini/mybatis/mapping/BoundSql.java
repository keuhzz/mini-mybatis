package com.zyuhoo.mini.mybatis.mapping;

import java.util.Map;

/**
 * About BoundSql.
 *
 * @since 0.0.1
 */
public class BoundSql {

    private String sql;
    private Map<Integer, String> parameter;
    private String parameterType;
    private String resultType;

    public BoundSql(String sql, Map<Integer, String> parameter, String parameterType, String resultType) {
        this.sql = sql;
        this.parameter = parameter;
        this.parameterType = parameterType;
        this.resultType = resultType;
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
