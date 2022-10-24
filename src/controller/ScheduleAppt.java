package controller;

import DBAccess.DBAppointments;
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

import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.Optional;
import java.util.ResourceBundle;

/** This class is the controller for the page that shows the appointment schedule */
public class ScheduleAppt implements Initializable {

    /** The TableColumn that will show each Appointment Title */
    @FXML
    public TableColumn titleCol;

    /** The TableColumn that will show each Appointment description */
    @FXML
    public TableColumn descCol;

    /** The TableColumn that will show each Appointment Location */
    @FXML
    public TableColumn locationCol;

    /** The TableColumn that will show the contact associated with each Appointment */
    @FXML
    public TableColumn contactCol;

    /** The TableColumn that will show each Appointment Type */
    @FXML
    public TableColumn typeCol;

    /** The TableColumn that will show each Appointment Start Date and Time */
    @FXML
    public TableColumn startCol;

    /** The TableColumn that will show each Appointment End Date and Time */
    @FXML
    public TableColumn endCol;

    /** The TableColumn that will show the customer ID associated with each Appointment */
    @FXML
    public TableColumn customerIdCol;

    /** The TableColumn that will show the User ID associated with each Appointment */
    @FXML
    public TableColumn userIdCol;

    /** The table that will hold all the appointments to be shown */
    @FXML
    private TableView apptTable;

    /** The TableColumn that will show each Appointment ID */
    @FXML
    private TableColumn apptIdCol;

    /** The date picker the user will use to filter appointments based on the chosen date. Either by month or by week */
    @FXML
    private DatePicker datePicker;

    /** A boolean that will reflect which of the two radio buttons is selected. The By Month radio button will be selected initially. */
    boolean monthRadio = true;

    /** A list of all appointments in the Database */
    ObservableList<Appointments> allAppointments = DBAppointments.getAllAppointments();

    /** This method sets the monthRadio boolean to true when the Radio button is selected. It also changes the datePicker value to make sure the onDateSelect method is called.
     *
     * @param actionEvent the By Month radio button is selected
     */
    @FXML
    public void onMonthRadio (ActionEvent actionEvent) {
        monthRadio = true;
        //LocalDate hold = datePicker.getValue();
        datePicker.setValue(datePicker.getValue().minusDays(1));
        datePicker.setValue(datePicker.getValue().plusDays(1));
        //datePicker.setValue(LocalDate.now());
        //datePicker.setValue(hold);
    }

    /** This method sets the monthRadio boolean to false when the By Week Radio button is selected. It also changes the datePicker value to make sure the onDateSelect method is called.
     *
     * @param actionEvent the By Week radio button is selected
     */
    @FXML
    public void onWeekRadio (ActionEvent actionEvent) {
        monthRadio = false;
        datePicker.setValue(datePicker.getValue().minusDays(1));
        datePicker.setValue(datePicker.getValue().plusDays(1));
        //LocalDate hold = datePicker.getValue();
        //datePicker.setValue(LocalDate.now());
        //datePicker.setValue(hold);
    }

    /** Calls the fillTable method whenever a new date is selected
     *
     * @param actionEvent a new date is selected via the DatePicker
     */
    @FXML
    public void onDateSelect (ActionEvent actionEvent) {
        fillTable();
    }

    /** Opens the AddAppointment window
     *
     * @param actionEvent Add New Appointment button is clicked
     */
    @FXML
    public void onNewAppt (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/AddAppointment.fxml"));
        loader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = loader.getRoot();
        stage.setTitle("Add New Appointment");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /** Deletes the selected appointment from the Database and updates the Table
     *
     * @param actionEvent  delete button is clicked
     */
    @FXML
    public void onDelete (ActionEvent actionEvent) throws SQLException {

        try {
            if (!apptTable.getSelectionModel().isEmpty()) {
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
            DBAppointments.deleteAppointment(((Appointments) apptTable.getSelectionModel().getSelectedItem()).getApptId());
            Alert deletedAlert = new Alert(Alert.AlertType.INFORMATION);
            deletedAlert.setTitle("Appointment Deleted");
            deletedAlert.setContentText("Apointment ID: " + ((Appointments) apptTable.getSelectionModel().getSelectedItem()).getApptId() + " has been cancelled. The Appointment Type was " + ((Appointments) apptTable.getSelectionModel().getSelectedItem()).getType());
            deletedAlert.showAndWait();

        } catch(NullPointerException nullPointerException) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No Appointment Selected");
            alert.setContentText("Please Select the Appointment you would like to Delete");
            alert.showAndWait();
            return;
        }

        fillTable();

    }

    /** Returns the User to the Customer Search page
     *
     * @param actionEvent the close button is clicked
     */
    @FXML
    public void onClose (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/CustomerPage.fxml"));
        loader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = loader.getRoot();
        stage.setTitle("The Customer Search Page");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /** Opens the UpdateAppointment Window and passes in the selected appointment. Also passes a false boolean so the UpdateAppointment window knows where to return the user to.
     *
     * @param actionEvent Update Selected Appointment button is clicked
     */
    @FXML
    public void onUpdate (ActionEvent actionEvent) throws IOException {

            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/UpdateAppointment.fxml"));
                loader.load();

                UpdateAppointment updateAppointmentController = loader.getController();
                updateAppointmentController.populateFields((Appointments) apptTable.getSelectionModel().getSelectedItem(), false);

                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Parent root = loader.getRoot();
                stage.setTitle("Update Existing Appointment");
                stage.setScene(new Scene(root));
                stage.show();

            } catch (NullPointerException nullPointerException) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("SELECTION ERROR");
                alert.setContentText("No Appointment Selected");
                alert.showAndWait();
                return;
            }
    }

    /** This method updates the table based on which Radio Button is selected. */
    public void fillTable () {

        ObservableList<Appointments> theseAppointments = FXCollections.observableArrayList();

        if (monthRadio) {
            for (Appointments a : allAppointments) {
                if (a.getStart().toLocalDateTime().getMonth() == datePicker.getValue().getMonth() && a.getStart().toLocalDateTime().getYear() == datePicker.getValue().getYear()) {
                    theseAppointments.add(a);
                }
            }
        }

        if (!monthRadio) {
            for (Appointments a : allAppointments) {
                if (a.getStart().toLocalDateTime().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) == datePicker.getValue().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)){
                    theseAppointments.add(a);
                }
            }
        }


        apptTable.setItems(theseAppointments);
        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("apptId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("apptDescription"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

    }

    /** Initializes the screen. Ran everytime this screen is opened. Populates the date picker with the current date and calls the fillTable method
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {

        datePicker.setValue(LocalDate.now());
        fillTable();
    }

}
