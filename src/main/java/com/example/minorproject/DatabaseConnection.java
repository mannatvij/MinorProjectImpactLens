package com.example.minorproject;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public Connection databaseLink;
    public Connection getConnection() {
        String databaseName = "minor_project";
        String databaseUser = "root";
        String databasePassword = "Jaigurudev@13";
        String url = "jdbc:mysql://localhost:3306/" + databaseName;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
            System.out.println("✅ Database Connection Successful!");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Database Connection Failed!");
        }
        return databaseLink;
    }


}
