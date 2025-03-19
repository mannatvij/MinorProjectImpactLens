module MinorProject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires org.json;


    opens com.example.minorproject to javafx.fxml;
    exports com.example.minorproject;
}