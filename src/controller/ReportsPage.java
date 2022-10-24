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

/** This class controls the Reports Page. The page show 3 different reports.*/
public class ReportsPage implements Initializable {

    /** The ChoiceBox used in the first report section where the user can choose which month to check */
    @FXML
    private ChoiceBox monthChoice;

    /** The ChoiceBox used in the first report section where the user can choose which type of appointment to check for */
    @FXML
    private ChoiceBox typeChoice;

    /** The text that will show the total number of appointments by Month and Type after the user selects a choice for each */
    @FXML
    private Text totalText;

    /** The combo box that the user will use to select which Contact's schedule they would like to view */
    @FXML
    private ComboBox contactCombo;

    /** The TableView that shows all the appointments and their information for the selected Contact */
    @FXML
    private TableView contactTable;

    /** The TableColumn that will show the appointment ID */
    @FXML
    private TableColumn apptIdCol;

    /** The TableColumn that will show the appointment title */
    @FXML
    private TableColumn titleCol;

    /** The TableColumn that will show the appointment type */
    @FXML
    private TableColumn typeCol;

    /** The TableColumn that will show the appointment description */
    @FXML
    private TableColumn descCol;

    /** The TableColumn that will show the appointment start date and time */
    @FXML
    private TableColumn startCol;

    /** The TableColumn that will show the appointment end date and time */
    @FXML
    private TableColumn endCol;

    /** The TableColumn that will show the customer ID associated with the appointment */
    @FXML
    private TableColumn customerIdCol;

    /** The text that will show how many appointments are scheduled for the next two weeks */
    @FXML
    private Text nextTwoText;

    /** The text that will show how many appointments were scheduled for the previous two weeks */
    @FXML
    private Text lastTwoText;


    /** A List of Strings for each month. Used to fill the monthChoice ChoiceBox */
    ObservableList<String> monthList = FXCollections.observableArrayList("Jan", "Feb", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");

    /** A List of Strings for each type. Used to fill the typeChoice ChoiceBox. */
    ObservableList<String> typeList = FXCollections.observableArrayList();

    /** A List of Strings for each contact name. Used to fill the contactCombo ComboBox */
    ObservableList<String> contactList = FXCollections.observableArrayList();

    /** Updates the totalText text when a new month is selected from the monthChoice choice box
     *
     * @param actionEvent selection is made in the monthChoice ChoiceBox
     */
    @FXML
    public void onMonthChoice (ActionEvent actionEvent) {
        updateTotal();
    }

    /** updates the totalText text when a new type is selected from the typeChoice choice box
     *
     * @param actionEvent selection is made in the typeChoice ChoiceBox
     */
    @FXML public void onTypeChoice (ActionEvent actionEvent) {
        updateTotal();
    }

    /** Once a selection is made in each ChoiceBox, the total number of appointments that fit the criteria is calculated and presented */
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

    /** Updates and fills the table with appointment information for the selected Contact
     *
     * @param actionEvent selection is made in the contactCombo ComboBox
     */
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

    /** Returns the user to the Customer Search page
     *
     * @param actionEvent
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



    /** Initialize the screen. Ran everytime this screen is opened. Counts and sets the amount of appointments scheduled for the next two weeks, and schedule in the previous two weeks.
     *
     * @param url
     * @param resourceBundle
     */
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
