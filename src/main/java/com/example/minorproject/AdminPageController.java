package com.example.minorproject;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AdminPageController {

    @FXML private Label nameLabel;
    @FXML private Label memberSinceLabel;
    @FXML private Label treesPlantedLabel;
    @FXML private Text descriptionText;
    @FXML private PieChart treePieChart;

    private final String apiURL = "https://sprout-app.thegoodapi.com/app/public/metrics?shop_name=namomonk.myshopify.com";
    private final int monthlyGoal = 100;

    public void initialize() {
        fetchDataFromAPI();
    }

    private void fetchDataFromAPI() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiURL))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(this::updateUIFromJSON)
                .exceptionally(ex -> {
                    ex.printStackTrace();
                    return null;
                });
    }

    private void updateUIFromJSON(String jsonResponse) {
        try {
            JSONObject json = new JSONObject(jsonResponse);
            JSONObject localeStrings = json.getJSONObject("locale_strings");

            String name = json.optString("name", "NamoMonk");
            String memberSince = json.optString("member_since", "N/A");
            int treesThisMonth = json.optInt("total_trees_current_month", 0);
            String description = localeStrings.optString("combined_description", "We are dedicated...");

            Platform.runLater(() -> {
                nameLabel.setText("Project: " + name);
                memberSinceLabel.setText("Member Since: " + memberSince);
                treesPlantedLabel.setText("Trees Planted This Month: " + treesThisMonth);
                descriptionText.setText(description);

                updatePieChart(treesThisMonth, monthlyGoal);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePieChart(int planted, int goal) {
        int remaining = Math.max(goal - planted, 0);
        PieChart.Data plantedData = new PieChart.Data("Planted", planted);
        PieChart.Data remainingData = new PieChart.Data("Remaining", remaining);

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(plantedData, remainingData);
        treePieChart.setData(pieChartData);
        treePieChart.setLegendVisible(false);
        treePieChart.setLabelsVisible(true);

        for (PieChart.Data data : pieChartData) {
            Tooltip tooltip = new Tooltip(data.getName() + ": " + (int) data.getPieValue());
            Tooltip.install(data.getNode(), tooltip);
        }

        // Delay the styling until everything is rendered
        Platform.runLater(() -> {
            treePieChart.applyCss();  // Force layout
            treePieChart.layout();    // Ensure nodes exist

            Platform.runLater(() -> {
                for (javafx.scene.Node node : treePieChart.lookupAll(".chart-pie-label")) {
                    if (node instanceof Text text) {
                        text.setFill(javafx.scene.paint.Color.WHITE); // Make it visible
                        text.setStyle("-fx-font-weight: bold;");      // Make it bold
                    }
                }
            });
        });
    }


}
