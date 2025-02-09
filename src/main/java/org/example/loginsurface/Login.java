package org.example.loginsurface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Login {

    @FXML
    private Button button;

    @FXML
    private TextField password;

    @FXML
    private TextField username;

    @FXML
    private Label wrongLogin;

    private Properties properties = new Properties();

    public Login() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
                return;
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void userLogin(ActionEvent event) throws IOException {
        checkLogin();
    }

    private void checkLogin() throws IOException {
        Main m = new Main();
        textfieldDesign();
        String storedUsername = properties.getProperty("username");
        String storedPassword = properties.getProperty("password");

        if (username.getText().equals(storedUsername) && password.getText().equals(storedPassword)) {
            wrongLogin.setText("Success!");
            m.changeScene("afterLogin.fxml");
        } else if (username.getText().isEmpty() && password.getText().isEmpty()) {
            wrongLogin.setText("Please enter your data!");
        } else {
            wrongLogin.setText("Access denied!");
        }
    }

    public void textfieldDesign() {
        if (username.isFocused()) {
            wrongLogin.setText("hallo");
            password.setStyle("-fx-background-color:green;" + "-fx-border-width:1px");
        } else if (password.isFocused()) {
            username.setStyle("-fx-background-color:red;" + "-f-border-width:1px");
            password.setStyle("-fx-background-color:#fff;" + "-fx-border-width:2px");
        }
    }
}