package DBAccess;

import Database.DBConnection;
import model.Appointments;
import model.Customers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;

/** This class contains all the methods to send commands to the database regarding Customers */
public class DBCustomers {

    /** This method returns a list of all the Customers in the Database
     *
     * @return customerList
     */
    public static ObservableList<Customers> getAllCustomers() {

        ObservableList<Customers> customerList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * from customers";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int customerId = rs.getInt("Customer_ID");
                String customerName = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalCode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                int divisionId = rs.getInt("Division_ID");
                Customers C = new Customers(customerId, customerName, address, postalCode, phone, divisionId);
                customerList.add(C);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return customerList;
    }

    /** This method takes a customer and inserts it into the Database using SQL
     *
     * @param c the new customer to be inserted
     */
    public static int insert(Customers c) throws SQLException {

        String sql = "INSERT INTO CUSTOMERS (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setInt(1, c.getCustomerId());
        ps.setString(2, c.getCustomerName());
        ps.setString(3, c.getAddress());
        ps.setString(4, c.getPostalCode());
        ps.setString(5, c.getPhone());
        ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(7, "User");
        ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(9, "User");
        ps.setInt(10, c.getDivisionId());

        int rowsAffected = ps.executeUpdate();
        return rowsAffected;

    }

    /** This method updates an existing customer in the database with a new customer object
     *
     * @param c the new customer used to update the customer in the database
     */
    public static int update(Customers c) throws SQLException {

        String sql = "UPDATE customers SET Customer_ID=?, Customer_Name=?, Address=?, Postal_Code=?, Phone=?, Create_Date=?, Created_By=?, Last_Update=?, Last_Updated_By=?, Division_ID=? WHERE Customer_ID=?";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setInt(1, c.getCustomerId());
        ps.setString(2, c.getCustomerName());
        ps.setString(3, c.getAddress());
        ps.setString(4, c.getPostalCode());
        ps.setString(5, c.getPhone());
        ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(7, "User");
        ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(9, "User");
        ps.setInt(10, c.getDivisionId());
        ps.setInt(11, c.getCustomerId());

        int rowsAffected = ps.executeUpdate();
        return rowsAffected;

    }

    /** This method takes a customer object and deletes the matching customer from the Database.
     *
     * @param c the Customer Object used to find the matching customer in the database
     */
    public static int delete(Customers c) throws SQLException {

        for (Appointments a : DBAppointments.getAllAppointments()) {
            if (a.getCustomerId() == c.getCustomerId()) {
                DBAppointments.deleteAppointment(a.getApptId());
            }
        }


        String sql = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setInt(1, c.getCustomerId());

        int rowsAffected = ps.executeUpdate();
        return rowsAffected;

    }

}
