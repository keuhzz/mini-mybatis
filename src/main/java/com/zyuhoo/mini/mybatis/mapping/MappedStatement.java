package com.zyuhoo.mini.mybatis.mapping;

/**
 * About MappedStatement.
 *
 * @since 0.0.1
 */
public class MappedStatement {

    private String id;

    private SqlCommandType sqlCommandType;

    private BoundSql boundSql;

    MappedStatement() {
    }

    // 建造者模式
    public static class Builder {

        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(String id, SqlCommandType sqlCommandType, BoundSql boundSql) {
            mappedStatement.id = id;
            mappedStatement.sqlCommandType = sqlCommandType;
            mappedStatement.boundSql = boundSql;
        }

        public MappedStatement build() {
            assert mappedStatement.id != null;
            return mappedStatement;
        }
    }

    public String getId() {
        return id;
    }

    public SqlCommandType getSqlCommandType() {
        return sqlCommandType;
    }

    public BoundSql getBoundSql() {
        return boundSql;
    }

}
