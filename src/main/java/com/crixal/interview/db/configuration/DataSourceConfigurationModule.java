package com.crixal.interview.db.configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class DataSourceConfigurationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DataSourceProvider.class).to(DataSourceProviderImpl.class).in(Singleton.class);
    }
}
