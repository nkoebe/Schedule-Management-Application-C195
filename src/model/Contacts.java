package model;

/** This class models the Contacts object */
public class Contacts {

    /** The Contact ID for the Contact */
    private int contactId;

    /** The Name for the Contact */
    private String contactName;

    /** The Email for the Contact */
    private String contactEmail;

    /** The Constructor for the Contacts object
     *
     * @param contactId the Contact ID for the contact
     * @param contactName the name for the contact
     * @param contactEmail the email for the contact
     */
    public Contacts (int contactId, String contactName, String contactEmail) {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    /**
     * @return the Contact ID
     */
    public int getContactId() {
        return contactId;
    }

    /**
     * @return the Contact name
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * @return the Contact email
     */
    public String getContactEmail() {
        return contactEmail;
    }
}
