module org.example {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;

    opens org.example to javafx.fxml;
    exports org.example;

    opens org.example.controller to javafx.fxml;
    exports org.example.controller;
}