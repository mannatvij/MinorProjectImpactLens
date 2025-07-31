package com.example.minorproject;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class ImpactTimelineController implements Initializable {

    @FXML private LineChart<String, Number> timelineChart;
    @FXML private BarChart<String, Number> barChart;
    @FXML private PieChart orgPieChart;
    @FXML private ComboBox<String> countryComboBox;
    @FXML private StackPane chartStack;
    @FXML private AreaChart<String, Number> stackedAreaChart;

    @FXML private Button lineChartBtn;
    @FXML private Button barChartBtn;
    @FXML private Button pieChartBtn;
    @FXML private Button stackedAreaChartBtn;

    // Data for plastic bottles collected
    private final String[] months = {"Dec 2024", "Jan 2025", "Feb 2025"};
    private final int[] bottlesCollected = {57790, 61474, 70780};

    private List<TreeData> allData = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadCSVData();
        setupCountryDropdown();
        displayTimelineChart("All");
        populateBarChart();
        populatePieChart();
        showLineChart(); // default chart
        updateStackedAreaChart(); // Populate data initially

    }

    private void loadCSVData() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/com/example/minorproject/ImpactTimeline.csv")))) {

            String line;
            reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String month = parts[0];
                String year = parts[1];
                String org = parts[2];
                String type = parts[3];
                int count = Integer.parseInt(parts[4]);
                String country = parts[5];
                allData.add(new TreeData(month, year, org, type, count, country));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupCountryDropdown() {
        Set<String> countries = allData.stream().map(TreeData::getCountry).collect(Collectors.toSet());
        List<String> options = new ArrayList<>(countries);
        Collections.sort(options);
        options.add(0, "All");

        countryComboBox.setItems(FXCollections.observableArrayList(options));
        countryComboBox.setValue("All");

        countryComboBox.setOnAction(e -> displayTimelineChart(countryComboBox.getValue()));
    }

    private void displayTimelineChart(String selectedCountry) {
        timelineChart.getData().clear();

        Map<String, Integer> dataMap = new TreeMap<>();

        for (TreeData data : allData) {
            if (selectedCountry.equals("All") || data.getCountry().equals(selectedCountry)) {
                String key = data.getMonthYear();
                dataMap.put(key, dataMap.getOrDefault(key, 0) + data.getTreesPlanted());
            }
        }

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Trees Planted");

        for (Map.Entry<String, Integer> entry : dataMap.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        timelineChart.getData().add(series);
    }

    private void populateBarChart() {
        barChart.getData().clear();

        Map<String, Map<String, Integer>> yearlyOrgData = new TreeMap<>();

        for (TreeData data : allData) {
            yearlyOrgData
                    .computeIfAbsent(data.getYear(), y -> new HashMap<>())
                    .merge(data.getOrganization(), data.getTreesPlanted(), Integer::sum);
        }

        Set<String> allOrgs = allData.stream().map(TreeData::getOrganization).collect(Collectors.toSet());

        for (String org : allOrgs) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(org);

            for (String year : yearlyOrgData.keySet()) {
                int count = yearlyOrgData.get(year).getOrDefault(org, 0);
                series.getData().add(new XYChart.Data<>(year, count));
            }

            barChart.getData().add(series);
        }
    }

    private void populatePieChart() {
        orgPieChart.getData().clear();

        Map<String, Integer> orgTotals = new HashMap<>();

        for (TreeData data : allData) {
            orgTotals.merge(data.getOrganization(), data.getTreesPlanted(), Integer::sum);
        }

        for (Map.Entry<String, Integer> entry : orgTotals.entrySet()) {
            orgPieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
    }


    @FXML
    private void showLineChart() {
        timelineChart.setVisible(true);
        barChart.setVisible(false);
        orgPieChart.setVisible(false);
        stackedAreaChart.setVisible(false);
    }

    @FXML
    private void showBarChart() {
        timelineChart.setVisible(false);
        barChart.setVisible(true);
        orgPieChart.setVisible(false);
        stackedAreaChart.setVisible(false);
    }

    @FXML
    private void showPieChart() {
        timelineChart.setVisible(false);
        barChart.setVisible(false);
        orgPieChart.setVisible(true);
        stackedAreaChart.setVisible(false);
    }

    @FXML
    private void updateStackedAreaChart() {
        // Hide other charts
        timelineChart.setVisible(false);
        barChart.setVisible(false);
        orgPieChart.setVisible(false);


        stackedAreaChart.setVisible(true);


        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Bottles Collected");


        for (int i = 0; i < months.length; i++) {
            series.getData().add(new XYChart.Data<>(months[i], bottlesCollected[i]));
        }


        stackedAreaChart.getData().clear();
        stackedAreaChart.getData().add(series);
    }

}