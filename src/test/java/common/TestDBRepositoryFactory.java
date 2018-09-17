package common;

import com.crixal.interview.db.configuration.DataSourceProvider;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.TransactionIsolationLevel;

import java.sql.SQLException;

public class TestDBRepositoryFactory {
    private final DataSourceProvider dataSourceProvider;

    public TestDBRepositoryFactory(DataSourceProvider dataSourceProvider) {
        this.dataSourceProvider = dataSourceProvider;
    }

    public Handle getConnection() {
        try {
            DBI dbi = new DBI(dataSourceProvider.getDataSource());

            Handle connection = dbi.open();
            connection.getConnection().setAutoCommit(false);
            connection.begin();
            connection.setTransactionIsolation(TransactionIsolationLevel.READ_COMMITTED);
            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
