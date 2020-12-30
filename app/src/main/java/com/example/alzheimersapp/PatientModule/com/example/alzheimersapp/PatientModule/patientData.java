package com.example.alzheimersapp.PatientModule;

public class patientData {

    //Declaring variables
    public String patientName, patientEmail, patientPassword, patientContact, caregiverEmail, caregiverContact;
//    public int patientContact;

    public patientData(){

    }

    public patientData(String patientName, String patientEmail, String patientPassword, String patientContact, String caregiverEmail, String caregiverContact) {
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.patientPassword = patientPassword;
        this.patientContact = patientContact;
        this.caregiverEmail = caregiverEmail;
        this.caregiverContact = caregiverContact;
    }
}
