package DBAccess;

import Database.DBConnection;
import model.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

/** This class contains all the methods to send commands to the database regarding Users */
public class DBUsers {

    /** This method returns a List of all the Users in the Database
     *
     * @return uList
     */
    public static ObservableList<Users> getAllUsers() {

        ObservableList<Users> ulist = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * from users";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int userId = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");
                Users U = new Users(userId, userName, password);
                ulist.add(U);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return ulist;
    }



}
