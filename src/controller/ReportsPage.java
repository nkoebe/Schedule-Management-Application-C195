package controller;

import DBAccess.DBAppointments;
import DBAccess.DBContacts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Appointments;

import javafx.event.ActionEvent;
import model.Contacts;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ResourceBundle;

public class ReportsPage implements Initializable {

    @FXML
    private ChoiceBox monthChoice;

    @FXML
    private ChoiceBox typeChoice;

    @FXML
    private Text totalText;

    @FXML
    private ComboBox contactCombo;

    @FXML
    private TableView contactTable;

    @FXML
    private TableColumn apptIdCol;

    @FXML
    private TableColumn titleCol;

    @FXML
    private TableColumn typeCol;

    @FXML
    private TableColumn descCol;

    @FXML
    private TableColumn startCol;

    @FXML
    private TableColumn endCol;

    @FXML
    private TableColumn customerIdCol;

    @FXML
    private Text nextTwoText;

    @FXML
    private Text lastTwoText;


    ObservableList<String> monthList = FXCollections.observableArrayList("Jan", "Feb", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
    ObservableList<String> typeList = FXCollections.observableArrayList();
    ObservableList<String> contactList = FXCollections.observableArrayList();

    @FXML
    public void onMonthChoice (ActionEvent actionEvent) {
        updateTotal();
    }

    @FXML public void onTypeChoice (ActionEvent actionEvent) {
        updateTotal();
    }

    public void updateTotal () {
        if (!monthChoice.getSelectionModel().isEmpty() && !typeChoice.getSelectionModel().isEmpty()) {
            int appointmentCount = 0;
            for (Appointments a : DBAppointments.getAllAppointments()) {
                if (a.getStart().toLocalDateTime().getMonth() == Month.of(monthList.indexOf(monthChoice.getValue()) + 1) && a.getType().equals(typeChoice.getValue())) {
                    appointmentCount++;
                }
            }
            totalText.setText(String.valueOf(appointmentCount));
        }


        return;
    }

    @FXML
    public void onContactCombo (ActionEvent actionEvent) {

        ObservableList<Appointments> contactsAppointments = FXCollections.observableArrayList();
        for (Contacts c : DBContacts.getAllContacts()) {
            if (c.getContactName().equals(contactCombo.getValue())) {
                for (Appointments a : DBAppointments.getAllAppointments()) {
                    if (c.getContactId() == a.getContactId()) {
                        contactsAppointments.add(a);
                    }
                }
            }
        }



        contactTable.setItems(contactsAppointments);
        apptIdCol.setCellValueFactory(new PropertyValueFactory<>("apptId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("apptDescription"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));

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




    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {

        int nextCount = 0;
        int lastCount = 0;

        for (Appointments a : DBAppointments.getAllAppointments()) {
            typeList.add(a.getType());
            if (a.getStart().toLocalDateTime().isBefore(LocalDateTime.now().plusDays(15))) {
                nextCount++;
            }
            if (a.getStart().toLocalDateTime().isAfter(LocalDateTime.now().minusDays(15))) {
                lastCount++;
            }
        }

        monthChoice.setItems(monthList);
        typeChoice.setItems(typeList);
        nextTwoText.setText(String.valueOf(nextCount));
        lastTwoText.setText(String.valueOf(lastCount));

        for (Contacts c : DBContacts.getAllContacts()) {
            contactList.add(c.getContactName());
        }

        contactCombo.setItems(contactList);



    }

}
