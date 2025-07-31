package com.example.minorproject;

import java.sql.Connection;
import java.sql.DriverManager;
public class TestDB {

        public static void main(String[] args) {
            String url = "jdbc:mysql://localhost:3306/minor_project";
            String user = "root";
            String password = "Jaigurudev@13";

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(url, user, password);
                System.out.println("✅ Database Connection Successful!");
            } catch (Exception e) {
                System.out.println("❌ Connection failed: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


