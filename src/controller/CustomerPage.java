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
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.ResourceBundle;

/** This is the controller for the first window that is opened after a successful login. Users are able to search for Customers by Name or Customer ID.
 * Customers will show up in a table. Users can open customer charts, open the complete appointment schedule, or add a new customer from here.
 */
public class CustomerPage implements Initializable {

    /** The TextField where the user can enter a name or partial name to search for in the Database */
    @FXML
    private TextField firstNameTF;

    /** The TextField where the user can enter a customer ID to search for in the Database */
    @FXML
    private TextField customerIdTF;

    /** The table column that will show the name of customers that fit the search criteria */
    @FXML
    private TableColumn nameColumn;

    /** The table column that will show the ID of customers that fit the search criteria */
    @FXML
    private TableColumn idColumn;

    /** The table column that will show the phone number of customers that fit the search criteria */
    @FXML
    private TableColumn phoneColumn;

    /** The table column that will show the address of customers that fit the search criteria */
    @FXML
    private TableColumn addressColumn;

    /** The table view that shows the information of customers that fit the search criteria */
    @FXML
    private TableView searchTable;


    /** The list of all customers from the Database */
    ObservableList<Customers> allCustomers = DBCustomers.getAllCustomers();

    /** This method receives the User ID from the login page and checks if the User has an upcoming appointment that starts soon
     *
     * @param userId The ID of the user that logged in
     */
    public void passUserId (int userId) {
        for (Appointments a : DBAppointments.getAllAppointments()) {
           if (userId == a.getUserId() && LocalDateTime.now().isAfter(a.getStart().toLocalDateTime().minusMinutes(15)) && LocalDateTime.now().isBefore(a.getStart().toLocalDateTime())) {
               DateTimeFormatter dtf = DateTimeFormatter.ofPattern("hh:mm a");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("UPCOMING APPOINTMENT STARTS SOON");
                alert.setContentText("You have an appointment today, " + a.getStart().toLocalDateTime().toLocalDate() + ", starting at " + a.getStart().toLocalDateTime().toLocalTime().format(dtf) + ". Appointment ID: " + a.getApptId() + " Customer ID: " + a.getCustomerId());
                alert.showAndWait();
                return;
           }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("NO UPCOMING APPOINTMENTS");
        alert.setContentText("You currently have no upcoming appointments within the next 15 minutes");
        alert.showAndWait();
    }


    /** This method searches the list of all Customers from the Database and checks if there is a customer with a matching ID or Name to what was entered in the text fields.
     *
     * FIRST LAMBDA EXPRESSIONS
     * In this method I used two lambda expressions to replace a for loop with two if statements within it. These lambda expressions now handle the search/comparison to find
     * the matching patient(s).
     *
     * @param actionEvent the search button is clicked
     */
    @FXML
    public void onSearchButton (ActionEvent actionEvent) throws IOException {

        ObservableList<Customers> searchList = FXCollections.observableArrayList();

        //another lambda attempt THIS ONE WORKED LAMBDA #1
        allCustomers.stream().filter(c -> !customerIdTF.getText().isEmpty() && c.getCustomerId() == Integer.parseInt(customerIdTF.getText())).forEach(c -> searchList.add(c));
        allCustomers.stream().filter(c -> !firstNameTF.getText().isEmpty() && c.getCustomerName().toLowerCase().contains(firstNameTF.getText().toLowerCase()) && customerIdTF.getText().isEmpty()).forEach(c -> searchList.add(c));

        searchTable.setItems(searchList);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

    }

    /** This method takes the selected Customer from the search table and opens their chart, by opening the Customer File Window. It also passes the chosen Customer to the
     * SpecificCustomer Controller.
     *
     * SECOND LAMBDA EXPRESSION
     * In this method I use another Lambda expression in a similar way to the first. The expression replaces a for loop with and if statement within. The lambda expression now handles
     * search for the selected Customer in the database
     *
     * @param actionEvent Open File button is clicked
     */
    @FXML
    public void onOpenFileButton (ActionEvent actionEvent) throws IOException {

        //LAMBDA #2 SUCCESS
        try {
            Customers chosenCustomer = allCustomers.stream().filter(c -> !searchTable.getSelectionModel().isEmpty() && searchTable.getSelectionModel().getSelectedItem().equals(c)).findFirst().get();
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
        catch (NoSuchElementException noSuchElementException) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("SELECTION ERROR");
            alert.setContentText("Please select a Customer to view");
            alert.showAndWait();
        }

    }

    /** This method opens the AddCustomer window
     *
     * @param actionEvent add button is clicked
     */
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

    /** This method opens the Reports window
     *
     * @param actionEvent Generate Reports button is clicked
     */
    @FXML
    public void onReportsButton (ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ReportsPage.fxml"));
        loader.load();

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = loader.getRoot();
        stage.setTitle("Reports Page");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /** This method opens the View Schedule window.
     *
     * @param actionEvent
     */
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



    /** Initialize the screen. Ran everytime this screen is opened.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }

}
