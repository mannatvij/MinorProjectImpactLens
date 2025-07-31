module MinorProject {
    requires javafx.fxml;
    requires java.sql;
    requires org.json;
    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.scene;
    requires javafx.web;
    requires java.desktop;
    requires java.net.http;


    opens com.example.minorproject to javafx.fxml;
    exports com.example.minorproject;
}