package com.example.minorproject;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class DashboardController implements Initializable {
    @FXML
    private Label total_bottles_registered;

    @FXML
    private Label total_trees_planted;

    @FXML
    private Label impactLabel;

    @FXML
    private ImageView imageView;

    @FXML
    private Button impactTimelineBtn;

    private final String API_URL = "https://sprout-app.thegoodapi.com/app/public/metrics?shop_name=namomonk.myshopify.com";
    private final int REFRESH_INTERVAL = 10000;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fetchImpactData();
        startAutoRefresh();
        Image impactImage = new Image(getClass().getResource("/com/example/minorproject/impact_lens.jpg").toExternalForm());
        imageView.setImage(impactImage);
    }


    public void updateImpactData(int treesPlanted, int plasticBottles) {
        Platform.runLater(() -> {
            total_trees_planted.setText("Trees Planted: " + treesPlanted);
            total_bottles_registered.setText("Plastic Bottles Removed: " + plasticBottles);
            impactLabel.setText("Data fetched successfully!");
        });
    }
    public void fetchImpactData() {
        new Thread(() -> {
            try {
                URL url = new URL(API_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                int responseCode = conn.getResponseCode();
                System.out.println("Response Code: " + responseCode);

                if (responseCode == 200) {
                    Scanner scanner = new Scanner(conn.getInputStream());
                    StringBuilder response = new StringBuilder();
                    while (scanner.hasNext()) {
                        response.append(scanner.nextLine());
                    }
                    scanner.close();
                    System.out.println("API Response: " + response.toString());

                    JSONObject jsonResponse = new JSONObject(response.toString());
                    int treesPlanted = jsonResponse.optInt("total_trees_planted", -1);
                    int plasticBottles = jsonResponse.optInt("total_bottles_registered", -1);
                    System.out.println("Parsed Data - Trees: " + treesPlanted + ", Bottles: " + plasticBottles);

                    updateImpactData(treesPlanted, plasticBottles);
                } else {
                    Platform.runLater(() -> System.out.println("Failed to load impact data"));
                }
                conn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                Platform.runLater(() -> System.out.println("Error fetching data"));
            }
        }).start();
    }


    private void startAutoRefresh() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                fetchImpactData();
            }
        }, REFRESH_INTERVAL, REFRESH_INTERVAL);
    }

    @FXML
    private void openImpactTimeline() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/minorproject/ImpactTimeline.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Impact Timeline");
        stage.setScene(new Scene(root));

        stage.show();
    }

    @FXML
    private void handleAdminClick() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("auth_page.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Admin Login");
        stage.setScene(new Scene(root));

        stage.show();
    }
    @FXML
    private void openImpactStory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/minorproject/ourimpact.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Impact Story");
            stage.setScene(new Scene(root));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
