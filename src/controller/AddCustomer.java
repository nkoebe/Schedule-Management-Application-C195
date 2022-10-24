package controller;

import DBAccess.DBCustomers;
import DBAccess.DBFirstLevelDivisions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customers;
import model.FirstLevelDivisions;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;


/** The AddCustomer class opens a window where the user can enter information for a new Customer and is the controller for the page
 * */
public class AddCustomer implements Initializable {

    /** The TextField that the user will use to enter a name for the new Customer */
    @FXML
    private TextField nameTF;

    /** The TextField that the user will use to enter an address for the new Customer */
    @FXML
    private TextField addressTF;

    /** The TextField that the user will use to enter the postal code for the new Customer */
    @FXML
    private TextField postalTF;

    /** The TextField that the user will use to enter a phone number for the new Customer */
    @FXML
    private TextField phoneTF;

    /** The Label that will appear next to the combo box that presents the first level divisions based on which County is chosen. */
    @FXML
    private Label stateLabel;

    /** The TextField that will hold the Customer ID. This number is auto-generated and cannot be changed by the user */
    @FXML
    private TextField customerIdTF;

    /** The Combo Box that the user will use to choose which Country for the new Customer */
    @FXML
    private ComboBox countryCombo;

    /** The Combo Box that the user will use to choose which First Level Division for the new Customer. Options are populated based on which County is selected */
    @FXML
    private ComboBox firstLevelCombo;


    /** A list of all first level divisions in the Database */
    ObservableList<FirstLevelDivisions> allDivisions = DBFirstLevelDivisions.getAllDivisions();

    /** A list of the 3 country options. Used to populate the options in the countryCombo ComboBox */
    ObservableList<String> countryOptions = FXCollections.observableArrayList("U.S.", "UK", "Canada");

    /** This method populates the firstLevelCombo ComboBox based on which County is selected. Before this, the firstLevelCombo and its label are hidden
     *
     * @param actionEvent when an option is selected in the countryCombo ComboBox
     */
    @FXML
    public void onSelect (ActionEvent actionEvent) {
        ObservableList<String> firstLevel = FXCollections.observableArrayList();

        if(countryCombo.getSelectionModel().getSelectedItem().toString() == "U.S.") {
            for (FirstLevelDivisions f : allDivisions) {
                if (f.getCountryId() == 1) {
                    firstLevel.add(f.getDivName());
                }
            }
        }
        else if(countryCombo.getSelectionModel().getSelectedItem().toString() == "UK") {
            for (FirstLevelDivisions f : allDivisions) {
                if (f.getCountryId() == 2) {
                    firstLevel.add(f.getDivName());
                }
            }
        }
        else if(countryCombo.getSelectionModel().getSelectedItem().toString() == "Canada") {
            for (FirstLevelDivisions f : allDivisions) {
                if (f.getCountryId() == 3) {
                    firstLevel.add(f.getDivName());
                }
            }
        }

        firstLevelCombo.setItems(firstLevel);
        firstLevelCombo.setVisible(true);
        stateLabel.setVisible(true);


    }

    /** This method takes the information entered into each field and uses it to create a new Customer and save it to the Database
     *
     * @param actionEvent clicking the save button
     */
    @FXML
    public void onSave (ActionEvent actionEvent) throws SQLException {
        if (nameTF.getText().isEmpty() || addressTF.getText().isEmpty() || postalTF.getText().isEmpty() || phoneTF.getText().isEmpty() || firstLevelCombo.getSelectionModel().isEmpty()) {
            System.out.println(firstLevelCombo.getSelectionModel().getSelectedItem());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("MISSING INFO");
            alert.setContentText("Please be sure to enter info for all of the fields");
            alert.showAndWait();
            return;
        }
        else {
            for (FirstLevelDivisions f : allDivisions) {
                if (f.getDivName() == firstLevelCombo.getSelectionModel().getSelectedItem()) {
                    int divId = f.getDivId();
                    Customers c = new Customers(Integer.parseInt(customerIdTF.getText()), nameTF.getText(), addressTF.getText(), postalTF.getText(), phoneTF.getText(), divId);
                    DBCustomers.insert(c);
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Customer Added!");
                    alert.setContentText("This customer has been added to the database");
                    alert.showAndWait();
                }
            }
        }
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerPage.fxml"));
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setTitle("The Customer Search Page");
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException ioException) {
            System.out.println("Exception: " + ioException);
        }

    }

    /** Returns the user to the Customer Search page
     *
     * @param actionEvent clicking the cancel button
     */
    @FXML
    public void onCancel (ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerPage.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("The Customer Search Page");
        stage.setScene(new Scene(root));
        stage.show();
    }



    /** Initialize the screen. Ran everytime this screen is opened. Populates the countryCombo ComboBox and generates/sets the Customer ID
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        countryCombo.setItems(countryOptions);
        customerIdTF.setText(String.valueOf(DBCustomers.getAllCustomers().get(DBCustomers.getAllCustomers().size() - 1).getCustomerId() + 1));

    }



}
