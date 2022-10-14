package model;

public class FirstLevelDivisions {

    private int divId;
    private String divName;
    private int countryId;

    public FirstLevelDivisions (int divId, String divName, int countryId) {
        this.divId = divId;
        this.divName = divName;
        this.countryId = countryId;
    }

    public int getDivId() {
        return divId;
    }

    public String getDivName() {
        return divName;
    }

    public int getCountryId() {
        return countryId;
    }
}
