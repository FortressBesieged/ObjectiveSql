package com.github.braisdom.funcsql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractQuery<T> implements Query<T> {

    protected final Class<T> domainModelClass;

    protected int limit = -1;
    protected int offset = -1;

    protected String projection;
    protected String filter;
    protected String orderBy;
    protected String groupBy;
    protected String having;

    public AbstractQuery(Class<T> domainModelClass) {
        this.domainModelClass = domainModelClass;
    }

    @Override
    public Query where(String filter, Object... args) {
        this.filter = String.format(filter, args);
        return this;
    }

    @Override
    public Query select(String... columns) {
        this.projection = String.join(", ", columns);
        return this;
    }

    @Override
    public Query limit(int limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public Query orderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    @Override
    public Query groupBy(String groupBy) {
        this.groupBy = groupBy;
        return this;
    }

    @Override
    public Query having(String having) {
        this.having = having;
        return this;
    }

    @Override
    public Query offset(int offset) {
        this.offset = offset;
        return this;
    }

    protected <C> List<C> executeInternally(Connection connection, Class<C> domainModelClass, String sql) throws SQLException {
        SQLExecutor sqlExecutor = Database.getSqlExecutor();
        return sqlExecutor.query(connection, sql, domainModelClass);
    }

    protected List<Row> executeRawInternally(String sql) throws SQLException {
        ConnectionFactory connectionFactory = Database.getConnectionFactory();
        SQLExecutor sqlExecutor = Database.getSqlExecutor();

        return sqlExecutor.query(connectionFactory.getConnection(), sql);
    }

    protected String getTableName(Class tableClass) {
        return Table.getTableName(tableClass);
    }
}
