package com.crixal.interview.db.configuration;

import javax.sql.DataSource;

public interface DataSourceProvider {
    DataSource getDataSource();
}
