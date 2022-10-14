package controller;

import DBAccess.DBAppointments;
import DBAccess.DBCustomers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointments;
import model.Customers;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class CustomerPage implements Initializable {

    @FXML
    private TextField firstNameTF;

    @FXML
    private TextField customerIdTF;

    @FXML
    private TableColumn nameColumn;

    @FXML
    private TableColumn idColumn;

    @FXML
    private TableColumn phoneColumn;

    @FXML
    private TableColumn addressColumn;

    @FXML
    private TableView searchTable;


    ObservableList<Customers> allCustomers = DBCustomers.getAllCustomers();
    int userId = -1;

    public void passUserId (int userId) {
        this.userId = userId;
        System.out.println("User ID after pass: " + userId);
        for (Appointments a : DBAppointments.getAllAppointments()) {
           if (userId == a.getUserId() && LocalDateTime.now().isAfter(a.getStart().toLocalDateTime().minusMinutes(15)) && LocalDateTime.now().isBefore(a.getStart().toLocalDateTime())) {
               DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm a");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("UPCOMING APPOINTMENT STARTS SOON");
                alert.setContentText("You have an appointment starting at " + a.getStart().toLocalDateTime().toLocalTime().format(dtf) + ". Appointment ID: " + a.getApptId() + " Customer ID: " + a.getCustomerId());
                alert.showAndWait();
           }
        }
    }

    @FXML
    public void onSearchButton (ActionEvent actionEvent) throws IOException {

        ObservableList<Customers> searchList = FXCollections.observableArrayList();

        for (Customers c : allCustomers) {
            if (!customerIdTF.getText().isEmpty() && c.getCustomerId() == Integer.parseInt(customerIdTF.getText())) {
                searchList.add(c);
            }
            else if (!firstNameTF.getText().isEmpty() && c.getCustomerName().toLowerCase().contains(firstNameTF.getText().toLowerCase()) && customerIdTF.getText().isEmpty()) {
                searchList.add(c);
            }
        }


        searchTable.setItems(searchList);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

    }

    @FXML
    public void onOpenFileButton (ActionEvent actionEvent) throws IOException {

        Customers chosenCustomer = null;

        for (Customers c : allCustomers) {
            if (!searchTable.getSelectionModel().isEmpty() && searchTable.getSelectionModel().getSelectedItem().equals(c)){
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
        catch (NullPointerException nullPointerException) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("SELECTION ERROR");
            alert.setContentText("Please select a Customer to view");
            alert.showAndWait();
        }

    }

    @FXML
    public void onAddButton (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/AddCustomer.fxml"));
        loader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = loader.getRoot();
        stage.setTitle("Create New Customer");
        stage.setScene(new Scene(root));
        stage.show();

    }

    public void onViewButton (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ScheduleAppt.fxml"));
        loader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = loader.getRoot();
        stage.setTitle("View Schedule");
        stage.setScene(new Scene(root));
        stage.show();
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }

}
