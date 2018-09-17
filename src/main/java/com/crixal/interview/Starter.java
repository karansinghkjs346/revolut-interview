package com.crixal.interview;

import com.crixal.interview.db.migration.DBMigration;
import com.crixal.interview.web.WebStarter;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Starter {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new MainModule());

        injector.getInstance(DBMigration.class).migrate();
        injector.getInstance(WebStarter.class).start();
    }
}
