package model;

/** This class models the Countries object */
public class Countries {


    /** The ID for the Country */
    private int id;

    /** The name of the Country */
    private String name;

    /** The Constructor for the Countries object
     *
     * @param id the Country ID
     * @param name the name of the Country
     */
    public Countries(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * @return the Country ID
     */
    public int getId() {
        return id;
    }

    /**
     * @return the Country name
     */
    public String getName() {
        return name;
    }
}
