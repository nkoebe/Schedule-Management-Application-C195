package DBAccess;

import Database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointments;

import java.sql.*;
import java.time.LocalDateTime;

/** This class contains all the methods to send commands to the database regarding Appointments */
public class DBAppointments {

    /** This method returns a List of all the Appointments in the Database
     *
     * @return appointmentList
     */
    public static ObservableList<Appointments> getAllAppointments() {

        ObservableList<Appointments> appointmentList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * from appointments";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int apptId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String apptDescription = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                Timestamp start = rs.getTimestamp("Start");
                Timestamp end = rs.getTimestamp("End");
                int customerId = rs.getInt("Customer_ID");
                int userId = rs.getInt("User_ID");
                int contactId = rs.getInt("Contact_ID");
                Appointments A = new Appointments(apptId, title, apptDescription, location, type, start, end, customerId, userId, contactId);
                appointmentList.add(A);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return appointmentList;
    }

    /** This method takes a new appointment and adds it to the database. It returns the int rowsAffected, which can be used to verify that some change was made via SQL to the database.
     *
     * @return rowsAffected
     *
     * @param a the new Appointment object
     */
    public static int addAppointment(Appointments a) throws SQLException {

        String sql = "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setInt(1, a.getApptId());
        ps.setString(2, a.getTitle());
        ps.setString(3, a.getApptDescription());
        ps.setString(4, a.getLocation());
        ps.setString(5, a.getType());
        //set start time at 6
        ps.setTimestamp(6, a.getStart());
        //set end time at 7
        ps.setTimestamp(7, a.getEnd());
        ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(9, "User");
        ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(11, "User");
        ps.setInt(12, a.getCustomerId());
        ps.setInt(13, a.getUserId());
        ps.setInt(14, a.getContactId());

        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /** This method updates an existing appointment in the database. It returns the int rowsAffected, which can be used to verify that some change was made via SQL to the database.
     *
     * @return rowsAffected
     *
     * @param a the updated Appointment object
     */
    public static int updateAppointment(Appointments a) throws SQLException {

        String sql = "UPDATE appointments SET Title=?, Description=?, Location=?, Type=?, Start=?, End=?, Create_Date=?, Created_By=?, Last_Update=?, Last_Updated_By=?, Customer_ID=?, User_ID=?, Contact_ID=? WHERE Appointment_ID = ?";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setString(1, a.getTitle());
        ps.setString(2, a.getApptDescription());
        ps.setString(3, a.getLocation());
        ps.setString(4, a.getType());
        ps.setTimestamp(5, a.getStart());
        ps.setTimestamp(6, a.getEnd());
        ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(8, "User");
        ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(10, "User");
        ps.setInt(11, a.getCustomerId());
        ps.setInt(12, a.getUserId());
        ps.setInt(13, a.getContactId());
        ps.setInt(14, a.getApptId());

        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /** This method deletes a specified appointment from the database. It returns the int rowsAffected, which can be used to verify that some change was made via SQL to the database.
     *
     * @return rowsAffected
     *
     * @param apId The ID of the appointment to be deleted
     */
    public static int deleteAppointment(int apId) throws SQLException {

        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setInt(1, apId);

        int rowsAffected = ps.executeUpdate();
        return rowsAffected;

    }

}
