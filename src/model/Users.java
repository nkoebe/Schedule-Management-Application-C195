package model;

/** This class models the Countries object */
public class Users {

    /** The ID of the User */
    private int id;

    /** The username of the User */
    private String name;

    /** The password for the user */
    private String password;

    /** The Constructor for the Users object
     * @param id The ID of the User
     * @param name The username of the User
     * @param password The password of the User
     */
    public Users (int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    /**
     * @return the User ID
     */
    public int getId() {
        return id;
    }

    /**
     * @return the username
     */
    public String getName() {
        return name;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }
}
