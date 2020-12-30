package com.example.alzheimersapp.CaregiverModule.PillReminder;

import java.util.Date;

public class Reminders {

    private String id;
    private String message;
    private Date remindDate;
    private String cgEmail;
    private String ptEmail;

    public Reminders(){}

    public Reminders(String id, String message, Date remindDate, String cgEmail, String ptEmail) {
        this.id = id;
        this.message = message;
        this.remindDate = remindDate;
        this.cgEmail = cgEmail;
        this.ptEmail = ptEmail;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Date getRemindDate() {
        return remindDate;
    }

    public String getCgEmail() {
        return cgEmail;
    }

    public String getPtEmail() {
        return ptEmail;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRemindDate(Date remindDate) {
        this.remindDate = remindDate;
    }

    public void setCgEmail(String cgEmail) {
        this.cgEmail = cgEmail;
    }

    public void setPtEmail(String ptEmail) {
        this.ptEmail = ptEmail;
    }
}
