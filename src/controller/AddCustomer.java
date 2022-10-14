package controller;

import DBAccess.DBCustomers;
import DBAccess.DBFirstLevelDivisions;
import Database.DBCustomerQueries;
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

public class AddCustomer implements Initializable {

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

    //onSave Action Event -- Save all entered info into Database, return to search page
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
                    Customers c = new Customers(Integer.parseInt(userIdTF.getText()), nameTF.getText(), addressTF.getText(), postalTF.getText(), phoneTF.getText(), divId);
                    DBCustomerQueries.insert(c);
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

    @FXML
    public void onCancel (ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerPage.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("The Customer Search Page");
        stage.setScene(new Scene(root));
        stage.show();
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        countryCombo.setItems(countryOptions);
        userIdTF.setText(String.valueOf(DBCustomers.getAllCustomers().get(DBCustomers.getAllCustomers().size() - 1).getCustomerId() + 1));

    }



}
