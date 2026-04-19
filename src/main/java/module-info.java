module com.hotel {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires itextpdf;

    opens com.hotel to javafx.fxml;
    opens com.hotel.controller to javafx.fxml;
    opens com.hotel.model to javafx.base;

    exports com.hotel;
      exports com.hotel.model;
}