package com.example.minorproject;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static Connection databaseLink;

    public static Connection getConnection() {
        String databaseName = "minor_project";
        String databaseUser = "root";
        String databasePassword = "Jaigurudev@13";
        String url = "jdbc:mysql://localhost:3306/" + databaseName;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            if (databaseLink == null || databaseLink.isClosed()) {
                databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
                System.out.println("✅ Database Connection Successful!");
            }
        } catch (Exception e) {
            System.err.println("❌ Database Connection Failed: " + e.getMessage());
            e.printStackTrace();
        }
        return databaseLink;
    }
}

