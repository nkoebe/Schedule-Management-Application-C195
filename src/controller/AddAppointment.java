package controller;

import DBAccess.DBAppointments;
import DBAccess.DBContacts;
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
import javafx.stage.Stage;
import model.Appointments;

import javafx.event.ActionEvent;
import model.Contacts;
import model.Customers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

/** The AddAppointment class opens a window where the user can enter information for a new appointment and is the controller for the page
 * */
public class AddAppointment implements Initializable {

    /** The TextField that will hold the appointment ID for the new appointment. This ID is auto generated and the user will not be able to edit it */
    @FXML
    public TextField apptIdTF;

    /** The TextField that the user will use to enter a title for the new appointment */
    @FXML
    public TextField titleTF;

    /** The TextField that the user will use to enter a description for the new appointment */
    @FXML
    public TextField descTF;

    /** The TextField that the user will use to enter a location for the new appointment */
    @FXML
    public TextField locationTF;

    /** The TextField that the user will use to enter a type for the new appointment */
    @FXML
    public TextField typeTF;

    /** The TextField that the user will use to enter a start time for the new appointment. Time must be entered in HH:MM format */
    @FXML
    public TextField startTF;

    /** The TextField that the user will use to enter an end time for the new appointment. Time must be entered in HH:MM format */
    @FXML
    public TextField endTF;

    /** The TextField that the user will use to enter a Customer ID for the new appointment */
    @FXML
    public TextField customerIdTF;

    /** The TextField that the user will use to enter a user ID for the new appointment */
    @FXML
    public TextField userIdTF;

    /** The DatePicker that the user will use to choose a Start Date for the new appointment */
    @FXML
    public DatePicker startDatePicker;

    /** The DatePicker that the user will use to choose an End Date for the new appointment */
    @FXML
    public DatePicker endDatePicker;

    /** The ChoiceBox that the user will use to choose either AM or PM for the new appointment start time */
    @FXML
    public ChoiceBox startChoice;

    /** The ChoiceBox that the user will use to choose either AM or PM for the new appointment end time */
    @FXML
    public ChoiceBox endChoice;

    /** The ComboBox that the user will use to choose the Contact for the new appointment. Choices autogenerated from Database */
    @FXML
    public ComboBox contactBox;

    /** contactList is an observable list of all the contacts in the Database */
    ObservableList<Contacts> contactList = DBContacts.getAllContacts();

    /** The boolean fromSpecificCustomer will indicate whether this page was opened from the SpecificCustomer page or from the ScheduleAppt page.
     * This will allow this class to return the user to the correct window once an Appointment is added or the close button is clicked */
    boolean fromSpecificCustomer = false;

    /** Receives the customer ID when the Add Appointment screen is opened from a specific customer chart. This also auto-fills the Customer ID Text field
     *
     * @param customerId is the Customer ID that is passed from the SpecificCustomer Page
     */
    public void receiveCustomer (String customerId) {
        fromSpecificCustomer = true;
        customerIdTF.setText(customerId);
        customerIdTF.setEditable(false);
    }


    /** This method Saves the information that is entered into all the fields as a new appointment and insert it to the Database. The method will check to make sure that
     * the start and end times fall within business hours, and that there is no overlap between appointments for the Customer. Alerts are presented if the user leaves
     * a field empty, or enters an invalid start/end time
     *
     * @param actionEvent clicking the Save button
     */
    @FXML
    public void onSave (ActionEvent actionEvent) throws SQLException {
        try {

            int idFinder = -1;
            for (Contacts c : contactList) {
                if (c.getContactName() == contactBox.getSelectionModel().getSelectedItem().toString()) {
                    idFinder = c.getContactId();
                }
            }

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm a");
            LocalTime startTime = LocalTime.parse(startTF.getText() + " " + startChoice.getValue(), dtf);
            LocalTime endTime = LocalTime.parse(endTF.getText() + " " + endChoice.getValue(), dtf);
            LocalDateTime startDateTime = LocalDateTime.of(startDatePicker.getValue(), startTime);
            LocalDateTime endDateTime = LocalDateTime.of(endDatePicker.getValue(), endTime);

            ZoneId estZoneId = ZoneId.of("America/New_York");
            ZoneId localZoneId = ZoneId.systemDefault();

            LocalTime openLT = LocalTime.of(8, 00);
            LocalDateTime openLDT = LocalDateTime.of(startDateTime.toLocalDate(), openLT);
            LocalTime closeLT = LocalTime.of(22, 00);
            LocalDateTime closeLDT = LocalDateTime.of(endDateTime.toLocalDate(), closeLT);

            ZonedDateTime sDT = ZonedDateTime.of(startDateTime, localZoneId);
            ZonedDateTime openDT = ZonedDateTime.of(openLDT, estZoneId);
            ZonedDateTime eDT = ZonedDateTime.of(endDateTime, localZoneId);
            ZonedDateTime closeDT = ZonedDateTime.of(closeLDT, estZoneId);

            if (!startDateTime.isBefore(endDateTime)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("TIME ERROR");
                alert.setContentText("The start date and time must be before the end date and time.");
                alert.showAndWait();
                return;
            }

            if (sDT.isBefore(openDT) || eDT.isAfter(closeDT)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("TIME ERROR");
                alert.setContentText("The Time you've selected if outside of our Business Hours. Please choose a time between 8:00AM and 10:00 PM EST.");
                alert.showAndWait();
                return;

            }

            for (Appointments a : DBAppointments.getAllAppointments()) {
                if (a.getCustomerId() == Integer.parseInt(customerIdTF.getText())) {
                    if (Timestamp.valueOf(startDateTime).before(a.getStart()) && Timestamp.valueOf(endDateTime).after(a.getStart())) {
                        overlapAlert();
                        return;
                    }
                    else if (Timestamp.valueOf(startDateTime).equals(a.getStart())) {
                        overlapAlert();
                        return;
                    }
                    else if (Timestamp.valueOf(startDateTime).after(a.getStart()) && Timestamp.valueOf(startDateTime).before(a.getEnd())) {
                        overlapAlert();
                        return;
                    }
                }
            }

            Appointments ourAppt = new Appointments(Integer.parseInt(apptIdTF.getText()), titleTF.getText(), descTF.getText(), locationTF.getText(), typeTF.getText(), Timestamp.valueOf(startDateTime), Timestamp.valueOf(endDateTime), Integer.parseInt(customerIdTF.getText()), 1, idFinder);
            DBAppointments.addAppointment(ourAppt);

        } catch (NullPointerException nullPointerException) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("SELECTION ERROR");
            alert.setContentText("Selection Missing - Please be sure all fields have been filled.");
            alert.showAndWait();
            return;
        } catch (DateTimeParseException dateTimeParseException) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("TIME ENTRY ERROR");
            alert.setContentText("Please enter start/end time in HH:mm format");
            alert.showAndWait();
            return;
        }

        try {
            if (fromSpecificCustomer) {
                ObservableList<Customers> customersList = DBCustomers.getAllCustomers();
                for (Customers c : customersList) {
                    if (c.getCustomerId() == Integer.parseInt(customerIdTF.getText())) {
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
                }
            }

            if (!fromSpecificCustomer) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/ScheduleAppt.fxml"));
                loader.load();

                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                Parent root = loader.getRoot();
                stage.setTitle("View Schedule");
                stage.setScene(new Scene(root));
                stage.show();
            }
        } catch (IOException ioException) {
            return;
        }


    }

    /** Returns the user to the screen they came from. This is determined by the fromSpecificCustomer boolean.
     *
     * @param actionEvent clicking the cancel button */
    @FXML
    public void onCancel (ActionEvent actionEvent) throws IOException {

        if (fromSpecificCustomer) {
            ObservableList<Customers> customersList = DBCustomers.getAllCustomers();
            for (Customers c : customersList) {
                if (c.getCustomerId() == Integer.parseInt(customerIdTF.getText())) {
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
            }
        }

        if (!fromSpecificCustomer) {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ScheduleAppt.fxml"));
            loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Parent root = loader.getRoot();
            stage.setTitle("View Schedule");
            stage.setScene(new Scene(root));
            stage.show();
        }


    }

    /** Presents the alert when a user tries to save an appointment that would overlap with another appointment for the same customer */
    public static void overlapAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Appointment Overlap");
        alert.setContentText("The appointment time you are trying to create will overlap with a pre-existing appointment for this customer. Please adjust start and end time.");
        alert.showAndWait();
    }

    /** Initializes the screen. Ran everytime this screen is opened. Populates the options in the contactBox and the startChoice and endChoice ChoiceBoxes. Auto-generates the new Appointment ID
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<Appointments> apptList = DBAppointments.getAllAppointments();
        ObservableList<String> contactsForCombo = FXCollections.observableArrayList();
        for (Contacts c : contactList) {
            contactsForCombo.add(c.getContactName());
        }
        contactBox.setItems(contactsForCombo);

        apptIdTF.setText(String.valueOf(apptList.get(apptList.size() - 1).getApptId() + 1));

        ObservableList<String> amPm = FXCollections.observableArrayList("AM", "PM");
        startChoice.setItems(amPm);
        endChoice.setItems(amPm);
    }

}
