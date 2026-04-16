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

public class NewUser {
    @FXML
    public Button cancelButton;
    @FXML
    public Button changeButton;
    @FXML
    public Button saveButton;
    @FXML
    private TextField nameField;
    @FXML
    private TextField mailField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField passwordField2;
    @FXML
    private TableView<NewUser> userTable;
    @FXML
    private TableColumn<NewUser, String> tName;
    @FXML
    private TableColumn<NewUser, String> tMail;
    @FXML
    private TableColumn<NewUser, String> tPhone;
    @FXML
    private TableColumn<NewUser, String> tPassword;
    private String name;
    private String mail;
    private String phone;
    private String password;
    private String password2;

    private boolean editMode = false;
    private String editOriginalName;

    public NewUser() {
    }

    public NewUser(String name, String mail, String phone, String password, String password2) {
        this.name = name;
        this.mail = mail;
        this.phone = phone;
        this.password = password;
        this.password2 = password2;
    }

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

            userTable.getItems().clear(); // törli a régit

            while (rs.next()) {
                NewUser user = new NewUser(
                        rs.getString("name"),
                        rs.getString("mail"),
                        rs.getString("phone"),
                        rs.getString("password"),
                        "" // password2 nem kell DB-ből
                );
                userTable.getItems().add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void ChangeButton(ActionEvent event) {
        NewUser selected = userTable.getSelectionModel().getSelectedItem();
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
        String name = getNameField().getText();
        String mail = getMailField().getText();
        String phone = getPhoneField().getText();
        String password = getPasswordField().getText();
        String password2 = getPasswordField2().getText();

        if (!phone.matches("[\\d/()+\\-]+")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Phone number is not valid!");
            alert.showAndWait();
            return;
        }

        if (!password.equals(password2)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Passwords do not match!");
            alert.showAndWait();
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
        try
        {
            Main m = new Main();
            m.changeScene("beforeLogin.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TextField getNameField() {
        return nameField;
    }

    public TextField getMailField() {
        return mailField;
    }

    public TextField getPhoneField() {
        return phoneField;
    }

    public TextField getPasswordField() {
        return passwordField;
    }

    public TextField getPasswordField2() {
        return passwordField2;
    }

    public String getName() {
        return name;
    }

    public String getMail() {
        return mail;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public String getPassword2() {
        return password2;
    }
}