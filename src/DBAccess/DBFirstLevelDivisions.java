package DBAccess;

import Database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.FirstLevelDivisions;

import java.sql.*;

/** This class contains all the methods to send commands to the database regarding First Level Divisions */
public class DBFirstLevelDivisions {

    /** This method returns a List of all the First Level Divisions in the Database
     *
     * @return divisionsList
     */
    public static ObservableList<FirstLevelDivisions> getAllDivisions() {

        ObservableList<FirstLevelDivisions> divisionsList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * from first_level_divisions";

            PreparedStatement ps = DBConnection.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int divId = rs.getInt("Division_ID");
                String divName = rs.getString("Division");
                int countryId = rs.getInt("Country_ID");
                FirstLevelDivisions F = new FirstLevelDivisions(divId, divName, countryId);
                divisionsList.add(F);
            }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        return divisionsList;
    }



}