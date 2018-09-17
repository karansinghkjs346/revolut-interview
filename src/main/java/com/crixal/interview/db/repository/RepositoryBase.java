package com.crixal.interview.db.repository;

import org.skife.jdbi.v2.Handle;

abstract class RepositoryBase {
    protected final Handle connection;

    public RepositoryBase(Handle connection) {
        this.connection = connection;
    }

    public void commit() {
        if (!connection.isInTransaction()) {
            throw new IllegalStateException("Connection is not in transaction");
        }

        connection.commit();
    }

    public void close() {
        try {
            if (connection.isInTransaction()) {
                connection.rollback();
            }
        } finally {
            connection.close();
        }
    }
}
