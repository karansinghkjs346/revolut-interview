package com.crixal.interview.web.controller;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

public class WebControllerConfigurationModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<WebController> multibinder = Multibinder.newSetBinder(binder(), WebController.class);
        multibinder.addBinding().to(UserController.class);
    }
}
