package com.crixal.interview.service;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class ServiceConfigurationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(UserService.class).to(UserServiceImpl.class).in(Singleton.class);
    }
}
