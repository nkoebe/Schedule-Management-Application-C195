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

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

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

    ResourceBundle rb = ResourceBundle.getBundle("Translation/translate", Locale.getDefault());

    public void onSubmit(ActionEvent actionEvent) throws IOException {

            //Checks to make sure a Username and Password are both entered - Alert if not
            if (userNameTF.getText().isEmpty() || passwordTF.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rb.getString("inputAlertTitle"));
                alert.setContentText(rb.getString("inputAlertText"));
                alert.showAndWait();
                return;
            }



            ObservableList<Users> userList = DBUsers.getAllUsers();

            //Go through usernames in the list and compare to the entered text
             /* for (Users u : userList) {
                if (u.getName().equals(userNameTF.getText())) {
                    //if found, compare this users password and the entered password
                    if (u.getPassword().equals(passwordTF.getText())) {
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
            } */

            //Attempt at Lambda - above for loop - worked
            try {
                Users currentUser = userList.stream().filter(u -> u.getName().equals(userNameTF.getText()) && u.getPassword().equals(passwordTF.getText())).findFirst().get();
                passwordMatch = true;

                String loginActivity = "C:/Users/noahk/IdeaProjects/demo/C195/login_activity.txt";
                //Scanner keyboard = new Scanner(System.in);
                FileWriter fWriter = new FileWriter(loginActivity, true);
                PrintWriter pWriter = new PrintWriter(fWriter);

                pWriter.println("Log in Attempt at " + Timestamp.valueOf(LocalDateTime.now()) + ". Username Entered: " + userNameTF.getText() + " Password Entered: " + passwordTF.getText() + " Log in Attempt was Successful.");
                pWriter.close();

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/CustomerPage.fxml"));
                loader.load();

                CustomerPage customerPageController = loader.getController();
                customerPageController.passUserId(currentUser.getId());

                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Parent root = loader.getRoot();
                stage.setTitle("The Customer Search Page");
                stage.setScene(new Scene(root));
                stage.show();

            } catch (NoSuchElementException noSuchElementException) {
                String loginActivity = "C:/Users/noahk/IdeaProjects/demo/C195/login_activity.txt";
                FileWriter fWriter = new FileWriter(loginActivity, true);
                PrintWriter pWriter = new PrintWriter(fWriter);
                pWriter.println("Log in Attempt at " + Timestamp.valueOf(LocalDateTime.now()) + ". Username Entered: " + userNameTF.getText() + " Password Entered: " + passwordTF.getText() + " Log in Attempt was Unsuccessful.");
                pWriter.close();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rb.getString("alertTitle"));
                alert.setContentText(rb.getString("alertText"));
                alert.showAndWait();
            }






            /* userList
                    .stream()
                    .filter(u -> u.getName().equals(userNameTF.getText()) && u.getPassword().equals(passwordTF.getText()))
                    .findFirst()
                    .get();






             if (!passwordMatch) {
                //If the password entered is incorrect, this alert will show up
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rb.getString("alertTitle"));
                alert.setContentText(rb.getString("alertText"));
                alert.showAndWait();
            } */

    }





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("I am initialized");

        String locale = Locale.getDefault().toString();
        System.out.println("locale " + locale);



        timeLabel.setText(rb.getString("location") + ": " + ZoneId.systemDefault());
        passwordText.setText(rb.getString("password"));
        userNameText.setText(rb.getString("username"));
        submitButton.setText(rb.getString("submit"));
        welcomeText.setText(rb.getString("welcome"));
        userNameTF.setPromptText(rb.getString("usernamePrompt"));
        passwordTF.setPromptText(rb.getString("passwordPrompt"));


    }

}
