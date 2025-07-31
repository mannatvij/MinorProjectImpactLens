package com.example.minorproject;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.io.File;

    public class ImpactMapApp extends Application {
        @Override
        public void start(Stage stage) {
            WebView webView = new WebView();

            String mapUrl = getClass().getResource("/com/example/minorproject/map.html").toExternalForm();
            webView.getEngine().load(mapUrl);

            stage.setScene(new Scene(webView, 1200, 800));
            stage.setTitle("Impact Lens - Tree Planting Map");
            stage.show();
        }

        public static void main(String[] args) {
            launch();
        }
    }
