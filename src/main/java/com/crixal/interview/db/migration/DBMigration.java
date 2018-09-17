package com.crixal.interview.db.migration;

import com.crixal.interview.db.configuration.DataSourceProvider;
import org.flywaydb.core.Flyway;

import javax.inject.Inject;

public class DBMigration {
    private DataSourceProvider dataSourceProvider;

    @Inject
    public DBMigration(DataSourceProvider dataSourceProvider) {
        this.dataSourceProvider = dataSourceProvider;
    }

    public void migrate() {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSourceProvider.getDataSource());
        flyway.setBaselineOnMigrate(true);
        flyway.setBaselineVersionAsString("0");
        flyway.setLocations("db/migration");
        flyway.migrate();
    }
}
