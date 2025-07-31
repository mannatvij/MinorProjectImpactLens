package com.example.minorproject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene; // Correct Scene class
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField; // Correct import
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthPageController {

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    public void handleLogin() {
        String inputPassword = passwordField.getText(); // Get actual text input

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement("SELECT password FROM admin WHERE name = 'NamoMonk'");
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String dbPassword = rs.getString("password");
                if (inputPassword.equals(dbPassword)) {
                    openAdminPage();
                    // Close login window
                    Stage stage = (Stage) passwordField.getScene().getWindow();
                    stage.close();
                } else {
                    errorLabel.setText("Incorrect password.");
                }
            } else {
                errorLabel.setText("Admin not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Error connecting to database.");
        }
    }

    private void openAdminPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin_page.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Admin Info");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



