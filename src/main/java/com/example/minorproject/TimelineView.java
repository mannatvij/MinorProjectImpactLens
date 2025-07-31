package com.example.minorproject;


    import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.*;

    public class TimelineView extends Application {

        private LineChart<String, Number> lineChart;
        private ComboBox<String> countryDropdown;
        private Map<String, Map<String, Integer>> countryData;

        @Override
        public void start(Stage stage) {
            stage.setTitle("Impact Timeline");

            // Setup X and Y axes
            final CategoryAxis xAxis = new CategoryAxis();
            final NumberAxis yAxis = new NumberAxis();
            xAxis.setLabel("Month");
            yAxis.setLabel("Trees Planted");

            lineChart = new LineChart<>(xAxis, yAxis);
            lineChart.setTitle("Trees Planted Over Time");

            // Dropdown for countries
            countryDropdown = new ComboBox<>();
            countryDropdown.getItems().add("All Countries");
            countryDropdown.getItems().addAll("Madagascar", "Kenya", "Indonesia", "Haiti", "Brazil");
            countryDropdown.setValue("All Countries");
            countryDropdown.setOnAction(e -> updateChart(countryDropdown.getValue()));

            // Load dummy data
            loadData();
            updateChart("All Countries");

            VBox root = new VBox(10, countryDropdown, lineChart);
            root.setPadding(new Insets(15));

            Scene scene = new Scene(root, 1000, 600);
            stage.setScene(scene);
            stage.show();
        }

        // Dummy data setup
        private void loadData() {
            countryData = new HashMap<>();

            // Simulated monthly data per country
            countryData.put("Madagascar", Map.of(
                    "Nov 2021", 1500, "Dec 2021", 1800, "Jan 2022", 2000, "Feb 2022", 2200
            ));
            countryData.put("Kenya", Map.of(
                    "Nov 2021", 1000, "Dec 2021", 1300, "Jan 2022", 1700, "Feb 2022", 1900
            ));
            countryData.put("Indonesia", Map.of(
                    "Nov 2021", 900, "Dec 2021", 1100, "Jan 2022", 1200, "Feb 2022", 1500
            ));
            countryData.put("Haiti", Map.of(
                    "Nov 2021", 800, "Dec 2021", 950, "Jan 2022", 1200, "Feb 2022", 1350
            ));
            countryData.put("Brazil", Map.of(
                    "Nov 2021", 1100, "Dec 2021", 1400, "Jan 2022", 1600, "Feb 2022", 1750
            ));
        }

        // Chart update method
        private void updateChart(String country) {
            lineChart.getData().clear();

            if (country.equals("All Countries")) {
                for (String c : countryData.keySet()) {
                    addSeries(c, countryData.get(c));
                }
            } else {
                Map<String, Integer> data = countryData.get(country);
                if (data != null) {
                    addSeries(country, data);
                }
            }
        }

        // Add series with tooltips
        private void addSeries(String country, Map<String, Integer> data) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(country);

            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                XYChart.Data<String, Number> point = new XYChart.Data<>(entry.getKey(), entry.getValue());
                series.getData().add(point);

                // Tooltip
                Tooltip tooltip = new Tooltip(country + "\n" + entry.getKey() + ": " + entry.getValue());
                tooltip.setShowDelay(Duration.millis(100));
                Tooltip.install(point.getNode(), tooltip);
            }

            lineChart.getData().add(series);
        }

        public static void main(String[] args) {
            launch(args);
        }
    }


