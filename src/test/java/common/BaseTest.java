package common;

import com.crixal.interview.MainModule;
import com.crixal.interview.db.configuration.DataSourceProvider;
import com.crixal.interview.db.migration.DBMigration;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.typesafe.config.ConfigFactory;
import org.skife.jdbi.v2.Handle;

import java.math.BigDecimal;

public abstract class BaseTest {
    protected static TestDBRepositoryFactory repositoryFactory;
    protected static Injector injector;

    static {
        ConfigFactory.invalidateCaches();
        System.setProperty("config.resource", "application-test.conf");
        injector = Guice.createInjector(new MainModule());
        repositoryFactory = new TestDBRepositoryFactory(injector.getInstance(DataSourceProvider.class));

        injector.getInstance(DBMigration.class).migrate();
    }

    protected void clearTestData() {
        try (Handle conn = repositoryFactory.getConnection()) {
            conn.execute("TRUNCATE TABLE user");
        }
    }

    protected void saveUser(String name, BigDecimal balance) {
        try (Handle conn = repositoryFactory.getConnection()) {
            conn.createCall("INSERT INTO user (name, balance, version) VALUES(:name, :balance, 0)")
                    .bind("name", name)
                    .bind("balance", balance)
                    .invoke();
            conn.commit();
        }
    }
}
