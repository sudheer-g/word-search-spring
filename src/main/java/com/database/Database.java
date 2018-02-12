package com.database;

import org.apache.commons.dbcp2.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;

public class Database {

    private static final BasicDataSource dataSource = new BasicDataSource();
    private static void setupDataSource(String connectURL) {
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(connectURL);
        dataSource.setUsername("root");
        dataSource.setPassword("123");
    }

    public static Connection connectToDatabase() {
        try {
            setupDataSource("jdbc:mysql://localhost:3306/wordSearch?autoReconnect=true&useSSL=false");
            return dataSource.getConnection();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
