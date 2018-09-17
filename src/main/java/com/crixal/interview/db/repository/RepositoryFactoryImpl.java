package com.crixal.interview.db.repository;

import com.crixal.interview.db.configuration.DataSourceProvider;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import javax.inject.Inject;
import java.sql.SQLException;

public class RepositoryFactoryImpl implements RepositoryFactory {
    private final DataSourceProvider dataSourceProvider;

    @Inject
    public RepositoryFactoryImpl(DataSourceProvider dataSourceProvider) {
        this.dataSourceProvider = dataSourceProvider;
    }

    @Override
    public UserRepository getUserRepository() {
        return new UserRepositoryImpl(getConnection());
    }

    private Handle getConnection() {
        try {
            DBI dbi = new DBI(dataSourceProvider.getDataSource());

            Handle connection = dbi.open();
            connection.getConnection().setAutoCommit(false);
            connection.begin();
            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
