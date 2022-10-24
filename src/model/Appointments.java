package model;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/** This class models the Appointments object */
public class Appointments {

    /** This is the Appointment ID of the Appointment */
    private int apptId;
    /** This is the Title of the Appointment */
    private String title;
    /** This is the Description of the Appointment */
    private String apptDescription;
    /** This is the Location of the Appointment */
    private String location;
    /** This is the Type of the Appointment */
    private String type;
    /** This is the Start Date and Time of the Appointment */
    private Timestamp start;
    /** This is the End Date and Time of the Appointment */
    private Timestamp end;
    /** This is the Customer ID associated with the Appointment */
    private int customerId;
    /** This is the User ID associated with the Appointment */
    private int userId;
    /** This is the Contact ID associated with the Appointment */
    private int contactId;

    /** This is the Constructor for the Appointments object
     *
     * @param apptId the Appointment ID of the Appointment
     * @param title the Title of the Appointment
     * @param apptDescription the Description of the Appointment
     * @param location the Location of the Appointment
     * @param type the Type of the Appointment
     * @param start the Start Date and Time of the Appointment
     * @param end the End Date and Time of the Appointment
     * @param customerId the Customer ID associated with the Appointment
     * @param userId the User ID associated with the Appointment
     * @param contactId the Contact ID associated with the Appointment
     */
    public Appointments (int apptId, String title, String apptDescription, String location, String type, Timestamp start, Timestamp end, int customerId, int userId, int contactId) {
        this.apptId = apptId;
        this.title = title;
        this.apptDescription = apptDescription;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
    }

    /**
     * @return the Appointment ID
     */
    public int getApptId() {
        return apptId;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the description
     */
    public String getApptDescription() {
        return apptDescription;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the start
     */
    public Timestamp getStart() {
        return start;
    }

    /**
     * @return the end
     */
    public Timestamp getEnd() {
        return end;
    }

    /**
     * @return the Customer ID
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @return the Contact ID
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * @return the User ID
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param apptId the appointment ID to set
     */
    public void setApptId(int apptId) {
        this.apptId = apptId;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @param apptDescription the description to set
     */
    public void setApptDescription(String apptDescription) {
        this.apptDescription = apptDescription;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @param start the start to set
     */
    public void setStart(Timestamp start) {
        this.start = start;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(Timestamp end) {
        this.end = end;
    }

    /**
     * @param customerId the customer ID to set
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * @param contactId the contact ID to set
     */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    /**
     * @param userId the user ID to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
