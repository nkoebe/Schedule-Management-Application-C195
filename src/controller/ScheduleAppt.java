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

public class ScheduleAppt implements Initializable {

    @FXML
    public TableColumn titleCol;

    @FXML
    public TableColumn descCol;

    @FXML
    public TableColumn locationCol;

    @FXML
    public TableColumn contactCol;

    @FXML
    public TableColumn typeCol;

    @FXML
    public TableColumn startCol;

    @FXML
    public TableColumn endCol;

    @FXML
    public TableColumn customerIdCol;

    @FXML
    public TableColumn userIdCol;

    @FXML
    private TableView apptTable;

    @FXML
    private TableColumn apptIdCol;

    @FXML
    private DatePicker datePicker;




    boolean monthRadio = true;
    ObservableList<Appointments> allAppointments = DBAppointments.getAllAppointments();

    @FXML
    public void onMonthRadio (ActionEvent actionEvent) {
        monthRadio = true;
        LocalDate hold = datePicker.getValue();
        datePicker.setValue(LocalDate.now());
        datePicker.setValue(hold);
    }

    @FXML
    public void onWeekRadio (ActionEvent actionEvent) {
        monthRadio = false;
        LocalDate hold = datePicker.getValue();
        datePicker.setValue(LocalDate.now());
        datePicker.setValue(hold);
    }

    @FXML
    public void onDateSelect (ActionEvent actionEvent) {
        fillTable();
    }

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

    @FXML
    public void onUpdate (ActionEvent actionEvent) throws IOException {
        //for (Appointments a : DBAppointments.getAllAppointments()) {
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



                /* if (apptTable.getSelectionModel().getSelectedItem().equals(a)) {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(getClass().getResource("/view/UpdateAppointment.fxml"));
                    loader.load();

                    UpdateAppointment updateAppointmentController = loader.getController();
                    updateAppointmentController.populateFields(a, false);

                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    Parent root = loader.getRoot();
                    stage.setTitle("Update Existing Appointment");
                    stage.setScene(new Scene(root));
                    stage.show();
                }*/
            } catch (NullPointerException nullPointerException) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("SELECTION ERROR");
                alert.setContentText("No Appointment Selected");
                alert.showAndWait();
                return;
            }
        //}
    }

    public void fillTable () {

        ObservableList<Appointments> theseAppointments = FXCollections.observableArrayList();

        if (monthRadio) {
            for (Appointments a : allAppointments) {
                if (a.getStart().toLocalDateTime().getMonth() == datePicker.getValue().getMonth() && a.getStart().toLocalDateTime().getYear() == datePicker.getValue().getYear())  /*a.getStart() in DateTime format has the same month as selected month from date picker */ {
                    theseAppointments.add(a);
                }
            }
        }

        if (!monthRadio) {
            for (Appointments a : allAppointments) {
                if (a.getStart().toLocalDateTime().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) == datePicker.getValue().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR)){
                    System.out.println("Correct Week");
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

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {

        datePicker.setValue(LocalDate.now());
        fillTable();
    }

}
