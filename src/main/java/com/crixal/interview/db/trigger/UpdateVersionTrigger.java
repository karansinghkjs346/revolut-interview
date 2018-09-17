package com.crixal.interview.db.trigger;

import org.h2.api.Trigger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateVersionTrigger implements Trigger {
    private String sequenceName;

    @Override
    public void init(Connection conn, String schemaName, String triggerName, String tableName, boolean before, int type) {
        this.sequenceName = tableName + "_version_seq";
    }

    @Override
    public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT nextval('" + sequenceName + "')")) {
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            newRow[3] = resultSet.getLong(1);
        }
    }

    @Override
    public void close() {
    }

    @Override
    public void remove() {
    }
}
