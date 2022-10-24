package model;

/** This class models the Customers object */
public class Customers {

    /** This is the Customer ID of the customer */
    private int customerId;

    /** This is the name of the customer */
    private String customerName;

    /** This is the address of the customer */
    private String address;

    /** This is the postal code of the customer */
    private String postalCode;

    /** This is the phone number of the customer */
    private String phone;

    /** This is the Division ID associated with the customer */
    private int divisionId;

    /** This is the Constructor for the Customers object
     *
     * @param customerId the Customer ID
     * @param customerName the Customer name
     * @param address the address of the customer
     * @param postalCode the postal code of the customer
     * @param phone the Customer's phone number
     * @param divisionId the Division ID associated with the customer
     */
    public Customers (int customerId, String customerName, String address, String postalCode, String phone, int divisionId) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
    }

    /**
     * @return the customer ID
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * @return the customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @return the address of the customer
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return the postal code of the customer
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @return the phone number of the customer
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @return the Division ID associated with the customer
     */
    public int getDivisionId() {
        return divisionId;
    }

    /**
     * @param customerId The customer ID to set
     */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    /**
     * @param customerName The customer name to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @param address The address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @param postalCode The postal code to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @param phone The phone number to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @param divisionId The division ID to set
     */
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }
}
