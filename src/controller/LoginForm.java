package controller;

import DBAccess.DBCountries;
import DBAccess.DBUsers;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Countries;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import model.Users;

public class LoginForm implements Initializable {

    @FXML
    private Text userNameText;

    @FXML
    private Text passwordText;

    @FXML
    private TextField userNameTF;

    @FXML
    private TextField passwordTF;

    @FXML
    private Label timeLabel;

    @FXML
    private Button submitButton;

    @FXML
    private Text welcomeText;

    boolean passwordMatch = false;



    public void onSubmit(ActionEvent actionEvent) throws IOException {

            //Checks to make sure a Username and Password are both entered - Alert if not
            if (userNameTF.getText().isEmpty() || passwordTF.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                if(Locale.getDefault().toString().compareTo("fr") == 0) {
                    alert.setTitle("Entrée manquante");
                    alert.setContentText("Vous avez oublié d'entrer le nom d'utilisateur ou le mot de passe");
                }
                else {
                    alert.setTitle("Missing Input");
                    alert.setContentText("You forgot to enter User Name or Password");
                }
                alert.showAndWait();
                return;
            }


            ObservableList<Users> userList = DBUsers.getAllUsers();

            //Go through usernames in the list and compare to the entered text
            for (Users u : userList) {
                if (u.getName().equals(userNameTF.getText())) {
                    //if found, compare this users password and the entered password
                    System.out.println("User name is " + u.getName() + " and ID is " + u.getId());
                    if (u.getPassword().equals(passwordTF.getText())) {
                        System.out.println("Correct Password");
                        passwordMatch = true;

                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(getClass().getResource("/view/CustomerPage.fxml"));
                        loader.load();

                        CustomerPage customerPageController = loader.getController();
                        customerPageController.passUserId(u.getId());

                        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        Parent root = loader.getRoot();
                        stage.setTitle("The Customer Search Page");
                        stage.setScene(new Scene(root));
                        stage.show();
                    }
                }
            }

            if (!passwordMatch) {
                //If the password entered is incorrect, this alert will show up
                Alert alert = new Alert(Alert.AlertType.ERROR);
                if(Locale.getDefault().toString().compareTo("fr") == 0) {
                    alert.setTitle("Identifiant ou mot de passe incorrect");
                    alert.setContentText("Le nom d'utilisateur ou le mot de passe est incorrect. Veuillez réessayer.");
                }
                else {
                    alert.setTitle("Incorrect User name or password");
                    alert.setContentText("User name or password is incorrect. Please try again.");
                }
                alert.showAndWait();
            }

    }





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("I am initialized");

        String locale = Locale.getDefault().toString();
        System.out.println("locale " + locale);
        if(Locale.getDefault().toString().compareTo("fr") == 0) {
            timeLabel.setText("Votre emplacement: " + ZoneId.systemDefault());
            passwordText.setText("Mot de passe");
            userNameText.setText("Nom d'utilisateur");
            submitButton.setText("Soumettre");
            timeLabel.setText("Votre emplacement: " + ZoneId.systemDefault());
            welcomeText.setText("Accueillir! Connectez-vous ici");
        }
        else {
            timeLabel.setText("Your location: " + ZoneId.systemDefault());
        }


        //System.out.println("Location: " + Locale.getDefault());

    }

}
