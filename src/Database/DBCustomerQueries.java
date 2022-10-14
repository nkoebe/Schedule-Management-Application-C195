package Database;

import DBAccess.DBAppointments;
import model.Appointments;
import model.Customers;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public abstract class DBCustomerQueries {

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

    public static int delete(Customers c) throws SQLException {

        //Delete matching SQL file. We'll figure it out. Need to delete appointments first? Foreign Keys and all that jazz
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
