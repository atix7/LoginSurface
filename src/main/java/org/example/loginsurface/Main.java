package org.example.loginsurface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    private static Stage stg;


    @Override
    public void start(Stage primarystage) throws IOException {
        stg = primarystage;
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("beforeLogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        primarystage.setTitle("Login app!");
        primarystage.setScene(scene);
        primarystage.show();
    }
    public void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
        stg.getScene().setRoot(pane);
    }

    public static void main(String[] args) {
        launch();
    }

}