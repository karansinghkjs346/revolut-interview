package com.crixal.interview.db.repository;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class RepositoryFactoryConfigurationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(RepositoryFactory.class).to(RepositoryFactoryImpl.class).in(Singleton.class);
    }
}
