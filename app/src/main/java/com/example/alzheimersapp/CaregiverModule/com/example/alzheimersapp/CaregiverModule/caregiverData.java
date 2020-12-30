package com.example.alzheimersapp.CaregiverModule;

public class caregiverData {

    //Declaring variables
    public String caregiverName, caregiverEmail, caregiverPassword, caregiverContact, patientEmail, patientContact;

    public caregiverData(){

    }

    public caregiverData(String caregiverName, String caregiverEmail, String caregiverPassword, String caregiverContact, String patientEmail, String patientContact) {
        this.caregiverName = caregiverName;
        this.caregiverEmail = caregiverEmail;
        this.caregiverPassword = caregiverPassword;
        this.caregiverContact =caregiverContact;
        this.patientEmail = patientEmail;
        this.patientContact = patientContact;
    }

}
