package controller;

import DBAccess.DBAppointments;
import DBAccess.DBCustomers;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Customers;
import model.Appointments;

import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class SpecificCustomer implements Initializable {

    @FXML
    private Text nameText;

    @FXML
    private Text customerIdText;

    @FXML
    private Text phoneText;

    @FXML
    private TableView appointmentTable;

    @FXML
    private TableColumn startCol;

    @FXML
    private TableColumn locationCol;

    @FXML
    private TableColumn apptIdCol;

    @FXML
    private TableColumn typeCol;

    @FXML
    private TableColumn descriptionCol;

    @FXML
    private TableColumn endCol;


    ObservableList<Appointments> appointmentList = DBAppointments.getAllAppointments();


    public void populateTable(Customers chosenCustomer) {

        nameText.setText(chosenCustomer.getCustomerName());
        customerIdText.setText(String.valueOf(chosenCustomer.getCustomerId()));
        phoneText.setText(chosenCustomer.getPhone());

        ObservableList<Appointments> customerAppointments = FXCollections.observableArrayList();

        fillTable(appointmentList);

        /* for (Appointments a : appointmentList) {
            if (a.getCustomerId() == chosenCustomer.getCustomerId()) {
                //display appt info in table
                customerAppointments.add(a);
            }
        }

        appointmentTable.setItems(customerAppointments);
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("apptId"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("apptDescription"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end")); */

    }



    @FXML
    public void onDeleteCustomer (ActionEvent actionEvent) throws SQLException {
        ObservableList<Customers> customerList = DBCustomers.getAllCustomers();
        for (Customers c : customerList) {
            if (c.getCustomerId() == Integer.parseInt(customerIdText.getText())) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Customer File Will Be Permanently Deleted from Database");
                alert.setContentText("Delete this Customer?");
                Optional<ButtonType> choice = alert.showAndWait();
                if (choice.get() == ButtonType.OK) {
                    DBCustomerQueries.delete(c);
                    Alert deletedAlert = new Alert(Alert.AlertType.INFORMATION);
                    deletedAlert.setTitle("Customer Deleted");
                    deletedAlert.setContentText("Customer ID: " + c.getCustomerId() + " (" + c.getCustomerName() + ") has been permanently deleted from the system");
                    deletedAlert.showAndWait();
                    break;
                };
                if (choice.get() == ButtonType.CANCEL) {
                    return;
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
    public void onUpdate (ActionEvent actionEvent) throws IOException {

        ObservableList<Customers> customerList = DBCustomers.getAllCustomers();


        for (Customers c : customerList) {
            //System.out.println("ID Check: " + c.getCustomerId() + " ID Text: " + Integer.parseInt(customerIdText.getText()));
            if (c.getCustomerId() == Integer.parseInt(customerIdText.getText())) {
                try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/view/UpdateCustomer.fxml"));
                    loader.load();

                    UpdateCustomer updateCustomerController = loader.getController();
                    updateCustomerController.populateFields(c);

                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    Parent root = loader.getRoot();
                    stage.setTitle("Update Existing Customer");
                    stage.setScene(new Scene(root));
                    stage.show();
                }
                catch (RuntimeException runtimeException) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("SELECTION ERROR");
                    alert.setContentText("Please select a Customer to view");
                    alert.showAndWait();
                }

            }
        }

    }

    @FXML
    public void onAddAppointment (ActionEvent actionEvent) throws IOException {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/AddAppointment.fxml"));
        loader.load();

        AddAppointment addAppointmentController = loader.getController();
        addAppointmentController.receiveCustomer(customerIdText.getText());

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = loader.getRoot();
        stage.setTitle("Add New Appointment");
        stage.setScene(new Scene(root));
        stage.show();

    }

    @FXML
    public void onUpdateAppointment (ActionEvent actionEvent) throws IOException {

            try{
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/UpdateAppointment.fxml"));
                loader.load();

                UpdateAppointment updateAppointmentController = loader.getController();
                updateAppointmentController.populateFields((Appointments) appointmentTable.getSelectionModel().getSelectedItem(), true);

                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Parent root = loader.getRoot();
                stage.setTitle("Update Existing Appointment");
                stage.setScene(new Scene(root));
                stage.show();
            }
            catch (NullPointerException nullPointerException) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("SELECTION ERROR");
                alert.setContentText("No Appointment Selected");
                alert.showAndWait();
            }

    }

    @FXML
    public void onDeleteAppointment (ActionEvent actionEvent) throws SQLException {

        /* ObservableList<Appointments> appointmentList = DBAppointments.getAllAppointments();
        Appointments ap = (Appointments) appointmentTable.getSelectionModel().getSelectedItem();
        DBAppointments.deleteAppointment(ap.getApptId()); */
        try {
            if(!appointmentTable.getSelectionModel().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Appointment?");
                alert.setContentText("Delete the selected appointment permanently?");
                Optional<ButtonType> choice = alert.showAndWait();
                if (choice.get() == ButtonType.OK) {
                }
                if (choice.get() == ButtonType.CANCEL) {
                    return;
                }
            }
            DBAppointments.deleteAppointment(((Appointments) appointmentTable.getSelectionModel().getSelectedItem()).getApptId());
            Alert deletedAlert = new Alert(Alert.AlertType.INFORMATION);
            deletedAlert.setTitle("Appointment Deleted");
            deletedAlert.setContentText("Apointment ID: " + ((Appointments) appointmentTable.getSelectionModel().getSelectedItem()).getApptId() + " has been cancelled. The Appointment Type was " + ((Appointments) appointmentTable.getSelectionModel().getSelectedItem()).getType());
            deletedAlert.showAndWait();

        } catch (NullPointerException nullPointerException) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Appointment Selected");
            alert.setContentText("Please Select the Appointment you would like to Delete");
            alert.showAndWait();
            return;
        }

        fillTable(DBAppointments.getAllAppointments());

    }

    public void fillTable(ObservableList<Appointments> remainingAppt) {

        ObservableList<Appointments> customerAppointments = FXCollections.observableArrayList();

        for (Appointments a : remainingAppt) {
            if (a.getCustomerId() == Integer.parseInt(customerIdText.getText())) {
                customerAppointments.add(a);
            }
        }

        appointmentTable.setItems(customerAppointments);
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("apptId"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("apptDescription"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));

    }


    @FXML
    public void onClose (ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerPage.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("The Customer Search Page");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }




}
