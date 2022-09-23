package controller;

import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class LoginForm implements Initializable {

    @FXML
    private Text userNameText;

    @FXML
    private Text passwordText;

    @FXML
    private TextField userNameTF;

    @FXML
    private TextField passwordTF;


    public void onSubmit(ActionEvent actionEvent) {

    }





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("I am initialized");
    }

}
