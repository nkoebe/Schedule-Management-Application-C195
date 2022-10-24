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

/** This class is the controller for the Specific Customer page. It is essentially a chart for the customer */
public class SpecificCustomer implements Initializable {

    /** This is the Text that will display the Customer's name */
    @FXML
    private Text nameText;

    /** This is the Text that will display the Customer's ID */
    @FXML
    private Text customerIdText;

    /** This is the Text that will display the Customer's phone number */
    @FXML
    private Text phoneText;

    /** This is the TableView that will display all scheduled appointments for the specific customer */
    @FXML
    private TableView appointmentTable;

    /** The column that displays the appointment start date and time */
    @FXML
    private TableColumn startCol;

    /** The column that displays the appointment location */
    @FXML
    private TableColumn locationCol;

    /** The column that displays the appointment ID*/
    @FXML
    private TableColumn apptIdCol;

    /** The column that displays the appointment type */
    @FXML
    private TableColumn typeCol;

    /** The column that displays the appointment description */
    @FXML
    private TableColumn descriptionCol;

    /** The column that displays the appointment end date and time */
    @FXML
    private TableColumn endCol;


    /** A list of all the appointments in the Database */
    ObservableList<Appointments> appointmentList = DBAppointments.getAllAppointments();


    /** This method receives the Customer that was selected from the Customer Search Page.
     * It then uses the Customer to display its information
     * @param chosenCustomer The customer selected and passed from the Customer Search page
     */
    public void populateTable(Customers chosenCustomer) {

        nameText.setText(chosenCustomer.getCustomerName());
        customerIdText.setText(String.valueOf(chosenCustomer.getCustomerId()));
        phoneText.setText(chosenCustomer.getPhone());

        fillTable(appointmentList);
    }


    /** This method deletes the current customer from the database and returns the user to the Customer Search page.
     *
     * @param actionEvent Delete This Customer Account button is clicked
     */
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
                    DBCustomers.delete(c);
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

    /** This method opens the UpdateCustomer window and passes the current customer to the UpdateCustomer controller
     *
     * @param actionEvent Update Customer is clicked
     */
    @FXML
    public void onUpdate (ActionEvent actionEvent) throws IOException {

        ObservableList<Customers> customerList = DBCustomers.getAllCustomers();


        for (Customers c : customerList) {
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

    /** Opens the AddAppointment window and passes in the current Customer ID, so that the appointment is scheduled correctly for the customer whose chart the user was in
     * This differs from when the AddAppointment window is opened elsewhere, as the user can enter any Customer ID in that case.
     *
     * @param actionEvent Add Appointment button is clicked
     */
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

    /** Opens the UpdateAppointment window and passes in the selected Appointment
     *
     * @param actionEvent Update Appointment button is clicked
     */
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

    /** This method deletes the selected appointment from the database and calls the fillTable method to update the TableView
     *
     * @param actionEvent delete appointment button is clicked
     */
    @FXML
    public void onDeleteAppointment (ActionEvent actionEvent) throws SQLException {
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

    /** This method fills and updates the TableView with the current appointments in the Database associated with the specific customer
     *
     * @param remainingAppt A list of the remaining appointments in the database
     */
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

    /** This method returns the user to the Customer Search page */
    @FXML
    public void onClose (ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/CustomerPage.fxml"));
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setTitle("The Customer Search Page");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /** Initialize the screen. Ran everytime this screen is opened.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }

}
