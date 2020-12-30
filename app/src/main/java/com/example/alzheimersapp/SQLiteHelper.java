package com.example.alzheimersapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.alzheimersapp.CaregiverModule.Caregiver;
import com.example.alzheimersapp.PatientModule.Patient;


public class SQLiteHelper extends SQLiteOpenHelper {

    //DATABASE NAME
    public static final String DATABASE_NAME = "AlzCure_DB";

    //DATABASE VERSION
    public static final int DATABASE_VERSION = 1;


    //CAREGIVER TABLE NAME
    public static final String TABLE_CAREGIVER_DATA = "Caregiver_Data";

    //TABLE USERS COLUMNS
    //CaregiverEmail COLUMN @primaryKey
    public static final String KEY_CG_EMAIL = "cgEmail";

    //COLUMN CaregiverName
    public static final String KEY_CG_NAME = "cgName";

    //COLUMN CaregiverPassword
    public static final String KEY_CG_PASSWORD = "cgPassword";

    //COLUMN CaregiverContact
    public static final String KEY_CG_CONTACT = "cgContact";

    //COLUMN PatientEmail
    public static final String KEY_P_EMAIL = "ptEmail";

    //COLUMN PatientEmail
    public static final String KEY_P_CONTACT = "ptContact";

    //SQL for creating users table
    public static final String SQL_TABLE_CAREGIVER = " CREATE TABLE " + TABLE_CAREGIVER_DATA
            + " ( "
            + KEY_CG_EMAIL + " TEXT PRIMARY KEY, "
            + KEY_CG_NAME + " TEXT, "
            + KEY_CG_PASSWORD + " TEXT, "
            +KEY_CG_CONTACT + " TEXT, "
            + KEY_P_EMAIL + " TEXT,"
            + KEY_P_CONTACT + " TEXT"
            + " ) ";

    //PATIENT TABLE NAME
    public static final String TABLE_PATIENT_DATA = "Patient_Data";

    //TABLE USERS COLUMNS
    //PatientEmail COLUMN @primaryKey
    public static final String KEY_PT_EMAIL = "ptEmail";

    //COLUMN PatientName
    public static final String KEY_PT_NAME = "ptName";

    //COLUMN PatientPassword
    public static final String KEY_PT_PASSWORD = "ptPassword";

    //COLUMN PatientContact
    public static final String KEY_PT_CONTACT = "ptContact";

    //COLUMN CaregiverEmail
    public static final String KEY_C_EMAIL = "cgEmail";

    //COLUMN CaregiverContact
    public static final String KEY_C_CONTACT = "cgContact";

    //SQL for creating patients table
    public static final String SQL_TABLE_PATIENT = " CREATE TABLE " + TABLE_PATIENT_DATA
            + " ( "
            + KEY_PT_EMAIL + " TEXT PRIMARY KEY, "
            + KEY_PT_NAME + " TEXT, "
            + KEY_PT_PASSWORD + " TEXT, "
            + KEY_PT_CONTACT + " TEXT, "
            + KEY_C_EMAIL + " TEXT,"
            + KEY_C_CONTACT + "TEXT"
            + " ) ";



    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Create Table when oncreate gets called
        sqLiteDatabase.execSQL(SQL_TABLE_CAREGIVER);
        sqLiteDatabase.execSQL(SQL_TABLE_PATIENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //drop table to create new one if database version updated
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_CAREGIVER_DATA);
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_PATIENT_DATA);
    }

    //CAREGIVER
    //using this method we can add caregiver to caregiver table
    public void addCaregiver(Caregiver caregiver) {

        //get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //create content values to insert
        ContentValues values = new ContentValues();

        //Put Caregiver Email in  @values
        values.put(KEY_CG_EMAIL, caregiver.cgEmail);

        //Put Caregiver Name in  @values
        values.put(KEY_CG_NAME, caregiver.cgName);

        //Put Caregiver Password in  @values
        values.put(KEY_CG_PASSWORD, caregiver.cgPassword);

        //Put Caregiver Contact in  @values
        values.put(KEY_CG_CONTACT, caregiver.cgContact);

        //Put Patient Email in  @values
        values.put(KEY_P_EMAIL, caregiver.ptEmail);

        //Put Patient Email in  @values
        values.put(KEY_P_CONTACT, caregiver.ptContact);

        // insert row
        long todo_id = db.insert(TABLE_CAREGIVER_DATA, null, values);
    }

    public Caregiver returnCaregiver(Caregiver caregiver) {
//        this.cgEmail = cgEmail;
        SQLiteDatabase db = this.getReadableDatabase();
        //Caregiver cg = new Caregiver();
        Cursor cursor = db.query(TABLE_CAREGIVER_DATA,// Selecting Table
                new String[]{KEY_CG_EMAIL, KEY_CG_NAME, KEY_CG_PASSWORD, KEY_CG_CONTACT, KEY_P_EMAIL, KEY_P_CONTACT},//Selecting columns want to query
                KEY_CG_EMAIL + "=?",
                new String[]{caregiver.cgEmail},//Where clause
                null, null, null);

        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            while (cursor.moveToFirst()){
                Caregiver cg = new Caregiver(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
                return cg;
            }

        }

        //if email does not exist return false
        return null;
    }


    //PATIENT
    //using this method we can add patient to patient table
    public void addPatient(Patient patient) {

        //get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //create content values to insert
        ContentValues values = new ContentValues();

        //Put Patient Email in  @values
        values.put(KEY_PT_EMAIL, patient.ptEmail);

        //Put Patient Name in  @values
        values.put(KEY_PT_NAME, patient.ptName);

        //Put Patient Password in  @values
        values.put(KEY_PT_PASSWORD, patient.ptPassword);

        //Put Patient Contact in  @values
        values.put(KEY_PT_CONTACT, patient.ptContact);

        //Put Caregiver Email in  @values
        values.put(KEY_C_EMAIL, patient.cgEmail);

        //Put Caregiver Contact in  @values
        values.put(KEY_C_CONTACT, patient.cgContact);

        // insert row
        long todo_id = db.insert(TABLE_PATIENT_DATA, null, values);
    }

    public Patient returnPatient(Patient patient) {

        SQLiteDatabase db = this.getReadableDatabase();
        //Caregiver cg = new Caregiver();
        Cursor cursor = db.query(TABLE_PATIENT_DATA,// Selecting Table
                new String[]{KEY_PT_EMAIL, KEY_PT_NAME, KEY_PT_PASSWORD, KEY_PT_CONTACT, KEY_C_EMAIL, KEY_C_CONTACT},//Selecting columns want to query
                KEY_PT_EMAIL + "=?",
                new String[]{patient.ptEmail},//Where clause
                null, null, null);

        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            while (cursor.moveToFirst()){
                Patient pt = new Patient(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
                return pt;
            }

        }

        //if email does not exist return false
        return null;
    }


}
