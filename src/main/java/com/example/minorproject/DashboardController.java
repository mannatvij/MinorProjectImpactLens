package com.example.minorproject;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import org.json.JSONObject;

public class DashboardController implements Initializable {
    @FXML
    private Label treesPlantedLabel;

    @FXML
    private Label plasticBottlesLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fetchImpactData(); // Fetch data when the dashboard loads
    }

    public void updateImpactData(int treesPlanted, int plasticBottles) {
        Platform.runLater(() -> {
            treesPlantedLabel.setText("Trees Planted: " + treesPlanted);
            plasticBottlesLabel.setText("Plastic Bottles Removed: " + plasticBottles);
        });
    }

    public void fetchImpactData() {
        String apiUrl = "https://56772816-de83-4392-8a58-6bc8e09823a8.mock.pstmn.io";

        new Thread(() -> {
            try {
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                int responseCode = conn.getResponseCode();
                System.out.println("Response Code: " + responseCode);

                if (conn.getResponseCode() == 200) {
                    Scanner scanner = new Scanner(conn.getInputStream());
                    StringBuilder response = new StringBuilder();
                    while (scanner.hasNext()) {
                        response.append(scanner.nextLine());
                    }
                    scanner.close();
                    System.out.println("API Response: " + response.toString());

                    JSONObject jsonResponse = new JSONObject(response.toString());
                    int treesPlanted = jsonResponse.optInt("treesPlanted", -1);
                    int plasticBottles = jsonResponse.optInt("plasticBottlesRemoved", -1);
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

        }

