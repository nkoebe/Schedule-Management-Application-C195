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

/** This class controls the Login Window */
public class LoginForm implements Initializable {

    /** The text that labels the username TextField */
    @FXML
    private Text userNameText;

    /** The text that labels the password TextField */
    @FXML
    private Text passwordText;

    /** The text field where the user will enter their Username */
    @FXML
    private TextField userNameTF;

    /** The text field where the user will enter their password */
    @FXML
    private TextField passwordTF;

    /** The label that shows the Location and ZoneID of the User */
    @FXML
    private Label timeLabel;

    /** The submit button */
    @FXML
    private Button submitButton;

    /** The welcome text at the top of the window*/
    @FXML
    private Text welcomeText;

    /** A resource bundle used to translate this page to French or english based on the Locale of the user */
    ResourceBundle rb = ResourceBundle.getBundle("Translation/translate", Locale.getDefault());

    /** This method verifies if both a username and a password are entered. It then searches the Database to verify if the information is correct.
     * If it is correct, it opens the CustomerPage and passes the User ID to it.
     * Each log in attempt is recorded in the login_activity.txt file.
     *
     * LAMBDA EXPRESSION #3
     * This lambda expression again eliminated a for loop with multiple if statements within. The expression handles searching the database for the entered User and verifying that the
     * password matches.
     *
     * @param actionEvent Submit button is clicked
     *
     * */
    public void onSubmit(ActionEvent actionEvent) throws IOException {

            if (userNameTF.getText().isEmpty() || passwordTF.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rb.getString("inputAlertTitle"));
                alert.setContentText(rb.getString("inputAlertText"));
                alert.showAndWait();
                return;
            }

            ObservableList<Users> userList = DBUsers.getAllUsers();

            //Attempt at Lambda - above for loop - worked
            try {
                Users currentUser = userList.stream().filter(u -> u.getName().equals(userNameTF.getText()) && u.getPassword().equals(passwordTF.getText())).findFirst().get();

                String loginActivity = "C:/Users/noahk/IdeaProjects/demo/C195/login_activity.txt";
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

    }



    /** Initialize the screen. Ran everytime this screen is opened. Set's all the text to the correct language based on the users locale.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        timeLabel.setText(rb.getString("location") + ": " + ZoneId.systemDefault());
        passwordText.setText(rb.getString("password"));
        userNameText.setText(rb.getString("username"));
        submitButton.setText(rb.getString("submit"));
        welcomeText.setText(rb.getString("welcome"));
        userNameTF.setPromptText(rb.getString("usernamePrompt"));
        passwordTF.setPromptText(rb.getString("passwordPrompt"));

    }

}
