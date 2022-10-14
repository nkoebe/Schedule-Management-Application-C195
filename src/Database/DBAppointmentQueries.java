package Database;

import model.Appointments;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class DBAppointmentQueries {

    public static int add (Appointments a) throws SQLException {

        String sql = "INSERT INTO appointments (Appointment_ID)";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setInt(1, a.getApptId());

        int rowsAffected = ps.executeUpdate();
        return rowsAffected;


    }





    public static int delete(Appointments a) throws SQLException {

        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = DBConnection.connection.prepareStatement(sql);
        ps.setInt(1, a.getApptId());

        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
}
