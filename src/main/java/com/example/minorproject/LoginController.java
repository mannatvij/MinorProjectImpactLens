package com.example.minorproject;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class LoginController implements Initializable {

    @FXML
    private Button cancelButton;

    @FXML
    private Label loginMessageLabel;

    @FXML
    private TextField UsernameTextField;

    @FXML
    private PasswordField enterPasswordField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void loginButtonOnAction(ActionEvent event) {
        if (!UsernameTextField.getText().isBlank() && !enterPasswordField.getText().isBlank()) {
            validateLogin();
        } else {
            loginMessageLabel.setText("Please enter the details");
        }
    }

    public void cancelButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void validateLogin() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        if (connectDB == null) {
            Platform.runLater(() -> loginMessageLabel.setText("Database connection failed!"));
            return;
        }

        String verifyLogin = "SELECT COUNT(1) FROM user_account WHERE username = ? AND password = ?";
        try (PreparedStatement preparedStatement = connectDB.prepareStatement(verifyLogin)) {
            preparedStatement.setString(1, UsernameTextField.getText());
            preparedStatement.setString(2, enterPasswordField.getText());
            ResultSet queryResult = preparedStatement.executeQuery();

            if (queryResult.next() && queryResult.getInt(1) == 1) {
                Platform.runLater(() -> loginMessageLabel.setText("Login Successful"));
                fetchImpactData();
            } else {
                Platform.runLater(() -> loginMessageLabel.setText("Invalid Login, try again"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(() -> loginMessageLabel.setText("Error connecting to DB"));
        } finally {
            try {
                if (connectDB != null) {
                    connectDB.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void fetchImpactData() {
        String apiUrl = "https://sprout-app.thegoodapi.com/app/public/metrics?shop_name=namomonk.myshopify.com";

        new Thread(() -> {
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                if (conn.getResponseCode() == 200) {
                    Scanner scanner = new Scanner(conn.getInputStream());
                    StringBuilder response = new StringBuilder();
                    while (scanner.hasNext()) {
                        response.append(scanner.nextLine());
                    }
                    scanner.close();

                    System.out.println("API Response: " + response.toString()); // Debugging

                    JSONObject jsonResponse = new JSONObject(response.toString());
                    int treesPlanted = jsonResponse.optInt("trees_planted", 0);
                    int plasticBottles = jsonResponse.optInt("plastic_bottles_removed", 0);

                    Platform.runLater(() -> openDashboard(treesPlanted, plasticBottles));

                } else {
                    Platform.runLater(() -> loginMessageLabel.setText("Failed to load impact data"));
                }
                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                Platform.runLater(() -> loginMessageLabel.setText("Error fetching data"));
            }
        }).start();
    }

    private void openDashboard(int treesPlanted, int plasticBottles) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
            Parent root = loader.load();

            DashboardController dashboardController = loader.getController();
            dashboardController.updateImpactData(treesPlanted, plasticBottles);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



