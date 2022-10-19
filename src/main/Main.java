package main;

import DBAccess.DBAppointments;
import Database.DBConnection;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Appointments;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

import static javafx.application.Application.launch;


public class Main extends Application {



    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginForm.fxml"));
        stage.setTitle("Login Form");
        if (Locale.getDefault().toString().compareTo("fr") == 0) {
            stage.setTitle("Formulaire de connexion");
        }
        stage.setScene(new Scene(root, 600, 500));
        stage.show();
    }

    public static void main(String[] args) {

        //Locale.setDefault(new Locale("fr"));

        DBConnection.openConnection();
        launch(args);
        DBConnection.closeConnection();


    }
}