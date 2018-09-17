package com.crixal.interview.web;

import com.crixal.interview.web.controller.WebControllerConfigurationModule;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class WebConfigurationModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new WebControllerConfigurationModule());

        bind(WebStarter.class).in(Singleton.class);
    }
}
