package org.example.loginsurface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class RegisterController {

    @FXML private TextField nameField;
    @FXML private TextField mailField;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField passwordField2;
    @FXML private Label statusLabel;

    private final UserService userService = new UserService();

    @FXML
    public void onRegister(ActionEvent event) {
        String name = nameField.getText();
        String mail = mailField.getText();
        String phone = phoneField.getText();
        String password = passwordField.getText();
        String password2 = passwordField2.getText();

        if (name.isEmpty() || mail.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            showStatus("All fields are required!", Color.web("#e74c3c"));
            return;
        }
        if (!phone.matches("[\\d/()+\\-]+")) {
            showStatus("Phone number is not valid!", Color.web("#e74c3c"));
            return;
        }
        if (!password.equals(password2)) {
            showStatus("Passwords do not match!", Color.web("#e74c3c"));
            return;
        }

        try {
            userService.saveUser(name, mail, phone, password);
            showStatus("Registration successful!", Color.web("#5fcd0b"));
            clearFields();
        } catch (Exception e) {
            showStatus("Registration failed!", Color.web("#e74c3c"));
            e.printStackTrace();
        }
    }

    @FXML
    public void onBack(ActionEvent event) {
        try {
            new Main().changeScene("beforeLogin.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showStatus(String message, Color color) {
        statusLabel.setTextFill(color);
        statusLabel.setText(message);
    }

    private void clearFields() {
        nameField.clear();
        mailField.clear();
        phoneField.clear();
        passwordField.clear();
        passwordField2.clear();
    }
}