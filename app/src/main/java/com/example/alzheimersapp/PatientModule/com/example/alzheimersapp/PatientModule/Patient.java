package com.example.alzheimersapp.PatientModule;

import java.util.ArrayList;

public class Patient {
    public String ptEmail;
    public String ptName;
    public String ptPassword;
    public String ptContact;
    public String cgEmail;
    public String cgContact;

    ArrayList<String> ptEmailList = new ArrayList<>();
    ArrayList<String> ptNameList = new ArrayList<>();
    ArrayList<String> ptPasswordList = new ArrayList<>();
    ArrayList<String> ptContactList = new ArrayList<>();
    ArrayList<String> cgEmailList = new ArrayList<>();
    ArrayList<String> cgContactList = new ArrayList<>();

    public Patient(){}

    public Patient(String ptEmail, String ptName, String ptPassword, String ptContact, String cgEmail, String cgContact) {
        this.ptEmail = ptEmail;
        this.ptName = ptName;
        this.ptPassword = ptPassword;
        this.ptContact = ptContact;
        this.cgEmail = cgEmail;
        this.cgContact = cgContact;
    }

    public ArrayList<String> getPtEmail() {
        return ptEmailList;
    }

    public ArrayList<String> getPtName() {
        return ptNameList;
    }

    public ArrayList<String> gePtPassword() {
        return ptPasswordList;
    }

    public ArrayList<String> getPtContact() {
        return ptContactList;
    }

    public String getCgEmail() {
        return cgEmail;
    }
    public String getCgContact() {
        return cgContact;
    }

    public void setPtEmail(String ptEmail) {
        this.ptEmailList.add(ptEmail);
    }

    public void setPtName(String ptName) {
        this.ptNameList.add(ptName);
    }

    public void setPtPassword(String ptPassword) {
        this.ptPasswordList.add(ptPassword);
    }

    public void setPtContact(String ptContact) {
        this.ptContactList.add(ptContact);
    }

    public void setCgEmail(String cgEmail) {
        this.cgEmailList.add(cgEmail);
    }
    public void setCgContact(String cgContact) {
        this.cgContactList.add(cgContact);
    }
}
