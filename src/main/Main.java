package main;

import Database.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main /* extends Application */ {

    public static void main(String[] args) {

        DBConnection.openConnection();
        DBConnection.closeConnection();
    }

    /* @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml"));
        stage.setTitle("Login Form");
        stage.setScene(new Scene(root, 600, 500));
        stage.show();
    }


    public static void main(String[] args){
        launch(args);
    }
    */

}