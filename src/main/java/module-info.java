module org.example.loginsurface {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.loginsurface to javafx.fxml;
    exports org.example.loginsurface;
}