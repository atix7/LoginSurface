module org.example.loginsurface {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires jbcrypt;

    opens org.example.loginsurface to javafx.fxml, javafx.controls;
    exports org.example.loginsurface;
}