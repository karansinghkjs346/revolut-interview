package com.crixal.interview.config;

public class ApplicationConfig {
    private Integer serverPort;
    private DBConfig db;

    public ApplicationConfig() {
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public DBConfig getDb() {
        return db;
    }

    public void setDb(DBConfig db) {
        this.db = db;
    }

    public static class DBConfig {
        private String url;
        private String user;
        private String password;

        public DBConfig() {
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
