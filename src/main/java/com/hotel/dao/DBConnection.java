package com.hotel.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static Connection connection = null;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Properties props = new Properties();
                InputStream input = DBConnection.class
                        .getClassLoader()
                        .getResourceAsStream("db.properties");

                if (input == null) {
                    System.err.println("db.properties file not found!");
                    return null;
                }

                props.load(input);

                String url = props.getProperty("db.url");
                String user = props.getProperty("db.user");
                String password = props.getProperty("db.password");

                connection = DriverManager.getConnection(url, user, password);
                System.out.println("Database connected successfully.");

            } catch (Exception e) {
                System.err.println("Database connection failed: " + e.getMessage());
            }
        }
        return connection;
    }
}