package model;

/** This class models the FirstLevelDivisions object */
public class FirstLevelDivisions {

    /** The Division ID for the FirstLevelDivision */
    private int divId;

    /** The name for the FirstLevelDivision */
    private String divName;

    /** The Country ID for the FirstLevelDivision */
    private int countryId;

    /** The Constructor for the FirstLevelDivisions object
     *
     * @param divId The Division ID
     * @param divName The division name
     * @param countryId The country ID associated with the division
     */
    public FirstLevelDivisions (int divId, String divName, int countryId) {
        this.divId = divId;
        this.divName = divName;
        this.countryId = countryId;
    }

    /**
     * @return the Division ID
     */
    public int getDivId() {
        return divId;
    }

    /**
     * @return the Division name
     */
    public String getDivName() {
        return divName;
    }

    /**
     * @return the Country ID associated with the division
     */
    public int getCountryId() {
        return countryId;
    }
}
