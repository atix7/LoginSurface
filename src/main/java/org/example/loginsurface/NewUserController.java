package org.example.loginsurface;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class NewUserController {

    @FXML private TextField nameField;
    @FXML private TextField mailField;
    @FXML private TextField phoneField;
    @FXML private TextField passwordField;
    @FXML private TextField passwordField2;

    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> tName;
    @FXML private TableColumn<User, String> tMail;
    @FXML private TableColumn<User, String> tPhone;
    @FXML private TableColumn<User, String> tPassword;

    private boolean editMode = false;
    private String editOriginalName;

    public void initialize() {
        userTable.setItems(FXCollections.observableArrayList());
        tName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tMail.setCellValueFactory(new PropertyValueFactory<>("mail"));
        tPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        loadUsersFromDatabase();
    }

    private void loadUsersFromDatabase() {
        String sql = "SELECT name, mail, phone, password FROM myuser";
        try (Connection conn = DriverManager.getConnection(DatabaseConn.DB_URL, DatabaseConn.USER, DatabaseConn.PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             var rs = pstmt.executeQuery()) {

            userTable.getItems().clear();
            while (rs.next()) {
                userTable.getItems().add(new User(
                        rs.getString("name"),
                        rs.getString("mail"),
                        rs.getString("phone"),
                        rs.getString("password")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ChangeButton(ActionEvent event) {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please select a row to change!");
            alert.showAndWait();
            return;
        }
        editOriginalName = selected.getName();
        editMode = true;
        nameField.setText(selected.getName());
        mailField.setText(selected.getMail());
        phoneField.setText(selected.getPhone());
        passwordField.clear();
        passwordField2.clear();
    }

    @FXML
    private void SaveButton(ActionEvent event) {
        String name = nameField.getText();
        String mail = mailField.getText();
        String phone = phoneField.getText();
        String password = passwordField.getText();
        String password2 = passwordField2.getText();

        if (!phone.matches("[\\d/()+\\-]+")) {
            showError("Phone number is not valid!");
            return;
        }
        if (!password.equals(password2)) {
            showError("Passwords do not match!");
            return;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        if (editMode) {
            String sql = "UPDATE myuser SET name=?, mail=?, phone=?, password=? WHERE name=?";
            try (Connection conn = DriverManager.getConnection(DatabaseConn.DB_URL, DatabaseConn.USER, DatabaseConn.PASS);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setString(2, mail);
                pstmt.setString(3, phone);
                pstmt.setString(4, hashedPassword);
                pstmt.setString(5, editOriginalName);
                pstmt.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
            editMode = false;
            editOriginalName = null;
        } else {
            String sql = "INSERT INTO myuser(name, mail, phone, password) VALUES (?, ?, ?, ?)";
            try (Connection conn = DriverManager.getConnection(DatabaseConn.DB_URL, DatabaseConn.USER, DatabaseConn.PASS);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setString(2, mail);
                pstmt.setString(3, phone);
                pstmt.setString(4, hashedPassword);
                pstmt.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        nameField.clear();
        mailField.clear();
        phoneField.clear();
        passwordField.clear();
        passwordField2.clear();
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

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}