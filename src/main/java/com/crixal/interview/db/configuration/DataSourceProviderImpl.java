package com.crixal.interview.db.configuration;

import com.crixal.interview.config.ApplicationConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.inject.Inject;
import javax.sql.DataSource;

public class DataSourceProviderImpl implements DataSourceProvider {
    private final ApplicationConfig applicationConfig;
    private final DataSource dataSource;

    @Inject
    public DataSourceProviderImpl(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
        this.dataSource = initDataSource();
    }

    private HikariDataSource initDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDataSourceClassName("org.h2.jdbcx.JdbcDataSource");
        hikariConfig.addDataSourceProperty("URL", applicationConfig.getDb().getUrl());
        hikariConfig.addDataSourceProperty("user", applicationConfig.getDb().getUser());
        hikariConfig.addDataSourceProperty("password", applicationConfig.getDb().getPassword());

        hikariConfig.setPoolName("HikariPool-Interview");
        hikariConfig.setMaximumPoolSize(8);
        hikariConfig.setMinimumIdle(2);
        return new HikariDataSource(hikariConfig);
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }
}
