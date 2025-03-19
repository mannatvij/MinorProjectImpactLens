package com.example.minorproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.InputStream;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            InputStream fxmlStream = getClass().getResourceAsStream("/com/example/minorproject/dashboard.fxml");

            if (fxmlStream == null) {
                throw new IOException("FXML file not found! Check path.");
            }

            Parent root = fxmlLoader.load(fxmlStream);
            DashboardController dashboardController = fxmlLoader.getController();

            if (dashboardController == null) {
                throw new NullPointerException("DashboardController is null! Check FXML fx:controller.");
            }

            dashboardController.fetchImpactData();  // Ensures API data is fetched when dashboard opens

            Scene scene = new Scene(root);
            stage.setTitle("Dashboard");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Print error in console
        }
    }

    public static void main(String[] args) {
        launch();
    }
}

