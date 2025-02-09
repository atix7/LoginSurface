package org.example.loginsurface;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.*;
public class NewUser {
    @FXML
    public Button cancelButton;
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
    }

    @FXML
    private void SaveButton(ActionEvent event) {
        String name = getNameField().getText();
        String mail = getMailField().getText();
        String phone = getPhoneField().getText();
        String password = getPasswordField().getText();
        String password2 = getPasswordField2().getText();
        if (!phone.matches("[\\d/\\()-]+")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Phone number is not valid!");
            alert.showAndWait();
            return;
        }

        if(!password.equals(password2)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Passwords do not match!");
            alert.showAndWait();
            return;
        }
        else {
            NewUser newUser = new NewUser(name, mail, phone, password, password2);
            userTable.getItems().add(newUser);
        }

        // Clear the text fields after saving
        nameField.clear();
        mailField.clear();
        phoneField.clear();
        passwordField.clear();
        passwordField2.clear();
    }
    @FXML
    private void cancelButton(ActionEvent event) {
        try
        {
            Main m = new Main();
            m.changeScene("afterLogin.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*@FXML
    void newUserForm(ActionEvent event) {
        try {
            Main m = new Main();
            m.changeScene("newUserForm.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

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