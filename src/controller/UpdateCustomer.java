package controller;

import DBAccess.DBCustomers;
import DBAccess.DBFirstLevelDivisions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customers;
import model.FirstLevelDivisions;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/** This class is the controller for the UpdateCustomer window*/
public class UpdateCustomer implements Initializable {

    /** The TextField that the user will use to change the name of Customer */
    @FXML
    private TextField nameTF;

    /** The TextField that the user will use to change the address of Customer */
    @FXML
    private TextField addressTF;

    /** The TextField that the user will use to change the postal code of Customer */
    @FXML
    private TextField postalTF;

    /** The TextField that the user will use to change the phone number of Customer */
    @FXML
    private TextField phoneTF;

    /** The TextField that the user will use to change the user ID associated with the Customer */
    @FXML
    private TextField userIdTF;

    /** The Combo Box that the user will use to change which Country for the Customer */
    @FXML
    private ComboBox countryCombo;

    /** The Combo Box that the user will use to change which First Level Division for the Customer */
    @FXML
    private ComboBox firstLevelCombo;

    /** A list of all the first level divisions in the Database */
    ObservableList<FirstLevelDivisions> allDivisions = DBFirstLevelDivisions.getAllDivisions();
    /** A list of the 3 Country options */
    ObservableList<String> countryOptions = FXCollections.observableArrayList("U.S.", "UK", "Canada");
    /** A list of all the Customers in the database */
    ObservableList<Customers> allCustomers = DBCustomers.getAllCustomers();

    /** This method receives the Customer from the previous window and uses it to populate all the information fields
     *
     * @param chosenCustomer the customer passed from the previous window
     */
    public void populateFields(Customers chosenCustomer) {
        userIdTF.setText(String.valueOf(chosenCustomer.getCustomerId()));
        nameTF.setText(chosenCustomer.getCustomerName());
        addressTF.setText(chosenCustomer.getAddress());
        postalTF.setText(chosenCustomer.getPostalCode());
        phoneTF.setText(chosenCustomer.getPhone());

        ObservableList<String> firstLevel = FXCollections.observableArrayList();
        for (FirstLevelDivisions f : allDivisions) {
            if (f.getDivId() == chosenCustomer.getDivisionId()) {
                if (f.getCountryId() == 1) {
                    countryCombo.setValue("U.S.");
                    for (FirstLevelDivisions fl : allDivisions) {
                        if (fl.getCountryId() == 1) {
                            firstLevel.add(fl.getDivName());
                        }
                    }
                }
                if (f.getCountryId() == 2) {
                    countryCombo.setValue("UK");
                    for (FirstLevelDivisions fl : allDivisions) {
                        if (fl.getCountryId() == 2) {
                            firstLevel.add(fl.getDivName());
                        }
                    }
                }
                if (f.getCountryId() == 3) {
                    countryCombo.setValue("Canada");
                    for (FirstLevelDivisions fl : allDivisions) {
                        if (fl.getCountryId() == 3) {
                            firstLevel.add(fl.getDivName());
                        }
                    }
                }
                firstLevelCombo.setItems(firstLevel);
                firstLevelCombo.setValue(f.getDivName());
                break;
            }
        }

    }

    /** This method updates the choices available in the first level division combo box based on the selection in the Country Combo box
     *
     * @param actionEvent Select button is clicked
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

    }

    /** This method updates the customer in the Database with the information entered into the fields. Then returns the user to the Specific Customer page.
     *
     * @param actionEvent save button is clicked
     */
    @FXML
    public void onSave (ActionEvent actionEvent) throws SQLException {

        for (Customers c : allCustomers) {
            if (c.getCustomerId() == Integer.parseInt(userIdTF.getText())) {
                c.setCustomerName(nameTF.getText());
                c.setAddress(addressTF.getText());
                c.setPostalCode(postalTF.getText());
                c.setPhone(phoneTF.getText());
                for (FirstLevelDivisions f : allDivisions) {
                    if(f.getDivName() == firstLevelCombo.getSelectionModel().getSelectedItem().toString()) {
                        c.setDivisionId(f.getDivId());
                    }
                }
                DBCustomers.update(c);

                try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/view/SpecificCustomer.fxml"));
                    loader.load();

                    SpecificCustomer specificCustomerController = loader.getController();
                    specificCustomerController.populateTable(c);

                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    Parent root = loader.getRoot();
                    stage.setTitle("Customer File");
                    stage.setScene(new Scene(root));
                    stage.show();
                }
                catch (IOException ioException) {
                    System.out.println("Exception: " + ioException);
                }
            }
        }

    }

    /** Returns the user to the Specific customer page without saving any changes
     *
     * @param actionEvent Cancel button is clicked
     */
    @FXML
    public void onCancel (ActionEvent actionEvent) throws IOException {

        Customers chosenCustomer = null;


        for (Customers c : allCustomers) {
            if (c.getCustomerId() == Integer.parseInt(userIdTF.getText())){
                chosenCustomer = c;
            }
        }

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/SpecificCustomer.fxml"));
            loader.load();

            SpecificCustomer specificCustomerController = loader.getController();
            specificCustomerController.populateTable(chosenCustomer);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Parent root = loader.getRoot();
            stage.setTitle("Customer File");
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (RuntimeException runtimeException) {
            System.out.println("Exception: " + runtimeException);
        }
    }

    /** Initialize the screen. Ran everytime this screen is opened. Populates the countryCombo ComboBox.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryCombo.setItems(countryOptions);
    }

}


