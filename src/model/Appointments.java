package model;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Appointments {

    private int apptId;
    private String title;
    private String apptDescription;
    private String location;
    private String type;
    private Timestamp start;
    private Timestamp end;
    private int customerId;
    private int userId;
    private int contactId;

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

    public int getApptId() {
        return apptId;
    }

    public String getTitle() {
        return title;
    }

    public String getApptDescription() {
        return apptDescription;
    }

    public String getLocation() {
        return location;
    }

    public String getType() {
        return type;
    }

    public Timestamp getStart() {
        return start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getContactId() {
        return contactId;
    }

    public int getUserId() {
        return userId;
    }

    public void setApptId(int apptId) {
        this.apptId = apptId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setApptDescription(String apptDescription) {
        this.apptDescription = apptDescription;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
