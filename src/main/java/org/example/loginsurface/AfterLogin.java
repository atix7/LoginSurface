package org.example.loginsurface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class AfterLogin {
    @FXML
    private Button logout;
    @FXML
    private Button newUser;
    @FXML
    public void userLogout(ActionEvent event) throws IOException{
        try{
            Main m = new Main();
            m.changeScene("beforeLogin.fxml");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @FXML
    void newUserForm(ActionEvent event) throws IOException {
        Main m = new Main();
        m.changeScene("newUserForm.fxml");
    }
}
