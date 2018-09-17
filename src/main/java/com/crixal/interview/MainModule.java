package com.crixal.interview;

import com.crixal.interview.config.ApplicationConfigConfigurationModule;
import com.crixal.interview.db.configuration.DataSourceConfigurationModule;
import com.crixal.interview.db.repository.RepositoryFactoryConfigurationModule;
import com.crixal.interview.service.ServiceConfigurationModule;
import com.crixal.interview.web.WebConfigurationModule;
import com.google.inject.AbstractModule;

public class MainModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new ApplicationConfigConfigurationModule());
        install(new DataSourceConfigurationModule());
        install(new WebConfigurationModule());
        install(new ServiceConfigurationModule());
        install(new RepositoryFactoryConfigurationModule());
    }
}
