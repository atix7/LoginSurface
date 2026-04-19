package org.example.loginsurface;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.paint.Color;

public class NewUserController {

    @FXML private TextField nameField;
    @FXML private TextField mailField;
    @FXML private TextField phoneField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML private Label stateLabel;

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> tName;
    @FXML private TableColumn<User, String> tMail;
    @FXML private TableColumn<User, String> tPhone;
    @FXML private TableColumn<User, String> tPassword;

    private final UserService userService = new UserService();
    private boolean editMode = false;
    private String editOriginalName;

    public void initialize() {
        userTable.setItems(FXCollections.observableArrayList());
        tName.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getName()));
        tMail.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getMail()));
        tPhone.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPhone()));
        tPassword.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPassword()));
        loadUsersFromDatabase();
    }

    private void loadUsersFromDatabase() {
        try {
            userTable.getItems().setAll(userService.loadUsers());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void DeleteButton(ActionEvent event) {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a row to delete!");
            return;
        }
        try {
            userService.deleteUser(selected.getName());
            showStatus("User deleted.", Color.web("#e74c3c"));
        } catch (Exception e) {
            showStatus("Delete failed!", Color.web("#e74c3c"));
            e.printStackTrace();
        }
        loadUsersFromDatabase();
    }

    @FXML
    private void ChangeButton(ActionEvent event) {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a row to change!");
            return;
        }
        editOriginalName = selected.getName();
        editMode = true;
        nameField.setText(selected.getName());
        mailField.setText(selected.getMail());
        phoneField.setText(selected.getPhone());
        passwordField.clear();
        confirmPasswordField.clear();
        showStatus("Editing: " + selected.getName(), Color.web("#DFC46C"));
        nameField.requestFocus();
    }

    @FXML
    private void SaveButton(ActionEvent event) {
        String name = nameField.getText();
        String mail = mailField.getText();
        String phone = phoneField.getText();
        String password = passwordField.getText();
        String password2 = confirmPasswordField.getText();

        if (name.isEmpty() || mail.isEmpty() || phone.isEmpty()) {
            showError("All fields are required!");
            return;
        }
        if (!editMode && password.isEmpty()) {
            showError("Password is required!");
            return;
        }
        if (!phone.matches("[\\d/()+\\-]+")) {
            showError("Phone number is not valid!");
            return;
        }
        if (!password.equals(password2)) {
            showError("Passwords do not match!");
            return;
        }

        try {
            if (editMode) {
                userService.updateUser(editOriginalName, name, mail, phone, password);
                editMode = false;
                editOriginalName = null;
                showStatus("User updated.", Color.web("#5fcd0b"));
            } else {
                userService.saveUser(name, mail, phone, password);
                showStatus("User saved.", Color.web("#5fcd0b"));
            }
        } catch (Exception e) {
            showStatus("Save failed!", Color.web("#e74c3c"));
            e.printStackTrace();
        }

        nameField.clear();
        mailField.clear();
        phoneField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        loadUsersFromDatabase();
    }

    @FXML
    private void cancelButton(ActionEvent event) {
        try {
            new Main().changeScene("beforeLogin.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showStatus(String message, Color color) {
        stateLabel.setTextFill(color);
        stateLabel.setText(message);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}