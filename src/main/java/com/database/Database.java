package com.database;

import org.apache.commons.dbcp2.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class Database {

    private static final BasicDataSource dataSource = new BasicDataSource();
    private static void setupDataSource(String connectURL) {
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(connectURL);
        dataSource.setUsername("root");
        dataSource.setPassword("123");
    }

    private static Connection connectToDatabase() {
        try {
            setupDataSource("jdbc:mysql://localhost:3306/wordSearch?autoReconnect=true&useSSL=false");
            return dataSource.getConnection();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean checkUserExists(String username, String password) {

        Connection con = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            con = Database.connectToDatabase();
            statement = con.createStatement();
            resultSet = statement.executeQuery("select * from users");
            while (resultSet.next()) {
                if (Objects.equals(resultSet.getString(1), username) && Objects.equals(resultSet.getString(2), password)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                if(con!=null) {
                    con.close();
                }
                if(statement!= null){
                    statement.close();
                }
                if(resultSet!=null) {
                    resultSet.close();
                }
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
