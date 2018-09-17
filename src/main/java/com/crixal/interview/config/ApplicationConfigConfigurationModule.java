package com.crixal.interview.config;

import com.google.inject.AbstractModule;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigBeanFactory;
import com.typesafe.config.ConfigFactory;

public class ApplicationConfigConfigurationModule extends AbstractModule {
    @Override
    protected void configure() {
        Config config = ConfigFactory.load().getConfig("app");
        ApplicationConfig applicationConfig = ConfigBeanFactory.create(config, ApplicationConfig.class);
        bind(ApplicationConfig.class).toInstance(applicationConfig);
    }
}
