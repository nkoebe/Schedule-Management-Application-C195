package controller;

import DBAccess.DBCustomers;
import DBAccess.DBFirstLevelDivisions;
import Database.DBCustomerQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateCustomer implements Initializable {

    @FXML
    private TextField nameTF;

    @FXML
    private TextField addressTF;

    @FXML
    private TextField postalTF;

    @FXML
    private TextField phoneTF;

    @FXML
    private Label stateLabel;

    @FXML
    private TextField userIdTF;

    @FXML
    private ComboBox countryCombo;

    @FXML
    private ComboBox firstLevelCombo;


    ObservableList<FirstLevelDivisions> allDivisions = DBFirstLevelDivisions.getAllDivisions();
    ObservableList<String> countryOptions = FXCollections.observableArrayList("U.S.", "UK", "Canada");
    ObservableList<Customers> allCustomers = DBCustomers.getAllCustomers();



    public void populateFields(Customers chosenCustomer) {
        System.out.println("Populate first");
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
                DBCustomerQueries.update(c);

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        countryCombo.setItems(countryOptions);
    }

}


