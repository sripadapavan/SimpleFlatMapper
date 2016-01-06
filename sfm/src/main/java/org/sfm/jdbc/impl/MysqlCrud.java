package org.sfm.jdbc.impl;

import org.sfm.jdbc.Crud;
import org.sfm.jdbc.JdbcMapper;
import org.sfm.jdbc.QueryPreparer;
import org.sfm.utils.RowHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class MysqlCrud<T, K> implements Crud<T, K> {
    private final BatchInsertQueryExecutor<T> batchInsertQueryExecutor;
    private final DefaultCrud<T, K> delegate;

    public MysqlCrud(DefaultCrud<T, K> delegate,
                     BatchInsertQueryExecutor<T> batchInsertQueryPreparer) {
        this.delegate = delegate;
        this.batchInsertQueryExecutor = batchInsertQueryPreparer;
    }

    @Override
    public void create(Connection connection, T value) throws SQLException {
        delegate.create(connection, value);
    }

    @Override
    public void create(Connection connection, Collection<T> values) throws SQLException {
        create(connection, values, null);
    }

    @Override
    public <RH extends RowHandler<? super K>> RH create(Connection connection, T value, RH keyConsumer) throws SQLException {
        return delegate.create(connection, value, keyConsumer);
    }

    @Override
    public <RH extends RowHandler<? super K>> RH create(Connection connection, Collection<T> values, final RH keyConsumer) throws SQLException {
        batchInsertQueryExecutor.insert(connection, values, new RowHandler<PreparedStatement>() {
            @Override
            public void handle(PreparedStatement preparedStatement) throws Exception {
                if (delegate.hasGeneratedKeys && keyConsumer != null) {
                    delegate.handleGeneratedKeys(keyConsumer, preparedStatement);
                }
            }
        });
        return keyConsumer;
    }

    @Override
    public T read(Connection connection, K key) throws SQLException {
        return delegate.read(connection, key);
    }

    @Override
    public <RH extends RowHandler<? super T>> RH read(Connection connection, Collection<K> keys, RH rowHandler) throws SQLException {
        return delegate.read(connection, keys, rowHandler);
    }

    @Override
    public void update(Connection connection, T value) throws SQLException {
        delegate.update(connection, value);
    }

    @Override
    public void update(Connection connection, Collection<T> values) throws SQLException {
        delegate.update(connection, values);
    }

    @Override
    public void delete(Connection connection, K key) throws SQLException {
        delegate.delete(connection, key);
    }

    @Override
    public void delete(Connection connection, List<K> keys) throws SQLException {
        delegate.delete(connection, keys);
    }

    @Override
    public void createOrUpdate(Connection connection, T value) throws SQLException {
        delegate.createOrUpdate(connection, value);
    }

    @Override
    public void createOrUpdate(Connection connection, Collection<T> values) throws SQLException {
        delegate.createOrUpdate(connection, values);
    }
}
