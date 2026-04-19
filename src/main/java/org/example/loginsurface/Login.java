package org.example.loginsurface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login {

    @FXML
    private TextField password;

    @FXML
    private TextField username;

    @FXML
    private Label wrongLogin;

    @FXML
    void userLogin(ActionEvent event) {
        checkLogin();
    }

    @FXML
    void openRegister(ActionEvent event) {
        try {
            new Main().changeScene("register.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkLogin() {
        String inputUser = username.getText();
        String inputPass = password.getText();

        if (inputUser.isEmpty() || inputPass.isEmpty()) {
            wrongLogin.setText("Please enter your data!");
            return;
        }

        String sql = "SELECT password FROM myuser WHERE name = ?";

        try (Connection conn = DriverManager.getConnection(DatabaseConn.DB_URL, DatabaseConn.USER, DatabaseConn.PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, inputUser);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    if (BCrypt.checkpw(inputPass, storedHash)) {
                        Main m = new Main();
                        m.changeScene("afterLogin.fxml");
                        return;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        wrongLogin.setTextFill(javafx.scene.paint.Color.web("#e74c3c"));
        wrongLogin.setText("Access denied!");
    }
}