package com.example.alzheimersapp.CaregiverModule;

import java.util.ArrayList;

public class Caregiver {

    public String cgEmail;
    public String cgName;
    public String cgPassword;
    public String cgContact;
    public String ptEmail;
    public String ptContact;

    ArrayList<String> cgEmailList = new ArrayList<>();
    ArrayList<String> cgNameList = new ArrayList<>();
    ArrayList<String> cgPasswordList = new ArrayList<>();
    ArrayList<String> cgContactList = new ArrayList<>();
    ArrayList<String> ptEmailList = new ArrayList<>();
    ArrayList<String> ptContactList = new ArrayList<>();


    public Caregiver(){}


    public Caregiver(String cgEmail, String cgName, String cgPassword, String cgContact, String ptEmail, String ptContact) {
        this.cgEmail = cgEmail;
        this.cgName = cgName;
        this.cgPassword = cgPassword;
        this.cgContact = cgContact;
        this.ptEmail = ptEmail;
        this.ptContact = ptContact;
    }

    public ArrayList<String> getCgEmail() {
        return cgEmailList;
    }

    public ArrayList<String> getCgName() {
        return cgNameList;
    }

    public ArrayList<String> getCgPassword() {
        return cgPasswordList;
    }

    public ArrayList<String> getCgContact() {
        return cgContactList;
    }

    public String getPtEmail() {
        return ptEmail;
    }
    public String getPtContact() {
        return ptContact;
    }

    public void setCgEmail(String cgEmail) {
        this.cgEmailList.add(cgEmail);
    }

    public void setCgName(String cgName) {
        this.cgNameList.add(cgName);
    }

    public void setCgPassword(String cgPassword) {
        this.cgPasswordList.add(cgPassword);
    }

    public void setCgContact(String cgContact) {
        this.cgContactList.add(cgContact);
    }

    public void setPtEmail(String ptEmail) {
        this.ptEmailList.add(ptEmail);
    }
    public void setPtContact(String ptContact) {
        this.ptContactList.add(ptContact);
    }
}

