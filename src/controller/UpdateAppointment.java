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
import model.Contacts;


import javafx.event.ActionEvent;
import model.Customers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class UpdateAppointment implements Initializable {


    @FXML
    private TextField apptIdTF;

    @FXML
    private TextField titleTF;

    @FXML
    private TextField descTF;

    @FXML
    private TextField locationTF;

    @FXML
    private TextField typeTF;

    @FXML
    private TextField startTF;

    @FXML
    private TextField endTF;

    @FXML
    private TextField customerIdTF;

    @FXML
    private TextField userIdTF;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ChoiceBox startChoice;

    @FXML
    private ChoiceBox endChoice;

    @FXML
    private ComboBox contactBox;



    ObservableList<Contacts> contactList = DBContacts.getAllContacts();
    boolean fromSpecificCustomer = false;

    public void populateFields(Appointments a, boolean b) {

        fromSpecificCustomer = b;
        LocalTime noonLt = LocalTime.of(12, 00);
        if (a.getStart().toLocalDateTime().toLocalTime().isBefore(noonLt)) {
            startChoice.setValue("AM");
        }
        if (!a.getStart().toLocalDateTime().toLocalTime().isBefore(noonLt)) {
            startChoice.setValue("PM");
        }
        if (a.getEnd().toLocalDateTime().toLocalTime().isBefore(noonLt)) {
            endChoice.setValue("AM");
        }
        if (!a.getEnd().toLocalDateTime().toLocalTime().isBefore(noonLt)) {
            endChoice.setValue("PM");
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm");


        apptIdTF.setText(String.valueOf(a.getApptId()));
        titleTF.setText(a.getTitle());
        descTF.setText(a.getApptDescription());
        locationTF.setText(a.getLocation());
        typeTF.setText(a.getType());
        startTF.setText(dtf.format(a.getStart().toLocalDateTime()));
        startDatePicker.setValue(a.getStart().toLocalDateTime().toLocalDate());
        endTF.setText(dtf.format(a.getEnd().toLocalDateTime()));
        endDatePicker.setValue(a.getEnd().toLocalDateTime().toLocalDate());
        customerIdTF.setText(String.valueOf(a.getCustomerId()));
        userIdTF.setText(String.valueOf(a.getUserId()));
        for (Contacts c : contactList) {
            if(c.getContactId() == a.getContactId()) {
                contactBox.setValue(c.getContactName());
            }
        }

    }

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

            if (sDT.isBefore(openDT) || eDT.isAfter(closeDT)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("TIME ERROR");
                alert.setContentText("The Time you've selected if outside of our Business Hours. Please choose a time between 8:00AM and 10:00 PM EST.");
                alert.showAndWait();
                return;

            }

            for (Appointments a : DBAppointments.getAllAppointments()) {
                if (a.getApptId() != Integer.parseInt(apptIdTF.getText())) {
                    if (a.getCustomerId() == Integer.parseInt(customerIdTF.getText())) {
                        if (Timestamp.valueOf(startDateTime).before(a.getStart()) && Timestamp.valueOf(endDateTime).after(a.getStart())) {
                            AddAppointment.overlapAlert();
                            return;
                        } else if (Timestamp.valueOf(startDateTime).equals(a.getStart())) {
                            AddAppointment.overlapAlert();
                            return;
                        } else if (Timestamp.valueOf(startDateTime).after(a.getStart()) && Timestamp.valueOf(startDateTime).before(a.getEnd())) {
                            AddAppointment.overlapAlert();
                            return;
                        }
                    }
                }
            }

            if (!startDateTime.isBefore(endDateTime)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("TIME ERROR");
                alert.setContentText("The start date and time must be before the end date and time.");
                alert.showAndWait();
                return;
            }

            if (startDateTime.isBefore(endDateTime)) {
                Appointments ourAppt = new Appointments(Integer.parseInt(apptIdTF.getText()), titleTF.getText(), descTF.getText(), locationTF.getText(), typeTF.getText(), Timestamp.valueOf(startDateTime), Timestamp.valueOf(endDateTime), Integer.parseInt(customerIdTF.getText()), 1, idFinder);
                DBAppointments.updateAppointment(ourAppt);
            }

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
            goBack(actionEvent);
        } catch (IOException ioException) {
            return;
        }


    }

    @FXML
    public void onCancel (ActionEvent actionEvent) throws IOException {
        goBack(actionEvent);
    }

    public void goBack(ActionEvent actionEvent) throws IOException {
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ObservableList<String> contactsForCombo = FXCollections.observableArrayList();
        for (Contacts c : contactList) {
            contactsForCombo.add(c.getContactName());
        }
        contactBox.setItems(contactsForCombo);

        ObservableList<String> amPm = FXCollections.observableArrayList("AM", "PM");
        startChoice.setItems(amPm);
        endChoice.setItems(amPm);

    }

}
