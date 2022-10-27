package com.example.oblig2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Class that handles all database handling. Other classes uses an instance of this class to
* communicate with the database. */

public class DBHandler extends SQLiteOpenHelper {

    static String TABLE_APPOINTMENTS = "Appointments";
    static String KEY_ID_APPOINTMENTS = "_ID";
    static String TITLE_APPOINTMENTS = "Title";
    static String DATE_APPOINTMENTS = "Date";
    static String TIME_APPOINTMENTS = "Time";
    static String PLACE_APPOINTMENTS = "Place";
    static String MSG_APPOINTMENTS = "Message";
    static String TABLE_CONTACTS = "Contacts";
    static String KEY_ID_CONTACTS = "_ID";
    static String NAME_CONTACTS = "Name";
    static String TEL_CONTACTS = "Tel";
    static int DATABASE_VERSION = 3;
    static String DATABASE_NAME = "DB_AppManager";

    // Database initialization
    public DBHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    // This method creates the tables "Appointments" and "Contacts" inside the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_CONTACTS = "CREATE TABLE " + TABLE_CONTACTS + "(" + KEY_ID_CONTACTS
                + " INTEGER PRIMARY KEY," + NAME_CONTACTS + " TEXT," + TEL_CONTACTS +
                " TEXT)";
        db.execSQL(CREATE_TABLE_CONTACTS);
        String CREATE_TABLE_APPOINTMENTS = "CREATE TABLE " + TABLE_APPOINTMENTS + "(" +
                KEY_ID_APPOINTMENTS + " INTEGER PRIMARY KEY," + TITLE_APPOINTMENTS + " TEXT,"
                + DATE_APPOINTMENTS + " DATE," + TIME_APPOINTMENTS + " TIME," +
                PLACE_APPOINTMENTS + " TEXT," + MSG_APPOINTMENTS + " TEXT)";
        db.execSQL(CREATE_TABLE_APPOINTMENTS);
    }

    // This method gets called when db needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENTS);
        onCreate(db);
    }

    // Method that receives a contact and stores its attributes in the "Contact" table
    public void addContact(SQLiteDatabase db, Contact contact){
        ContentValues values = new ContentValues();     // Variable that holds contact data
        values.put(NAME_CONTACTS, contact.getName());
        values.put(TEL_CONTACTS, contact.getTel());
        db.insert(TABLE_CONTACTS,null,values);  // Contact  data gets inserted  to db
    }

    // Method that retrieves all data from db and returns a list containing all contacts
    public List<Contact> printContacts(SQLiteDatabase db) {
        List<Contact> contactList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CONTACTS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(cursor.getInt(0));
                contact.setName(cursor.getString(1));
                contact.setTel(cursor.getString(2));
                contactList.add(contact);
            }
            while (cursor.moveToNext());        // Next row in db until there are no more
            cursor.close();
        }
        return contactList;
    }

    // Method that deletes a contact in db based on its id
    public void deleteContact(SQLiteDatabase db, int inId) {
        db.delete(TABLE_CONTACTS, KEY_ID_CONTACTS + " =? ",
            new String[]{Long.toString(inId)});
    }

    // Method that updates/modifies a contact in db based on its id
    public void updateContact(SQLiteDatabase db, Contact contact) {
        ContentValues values = new ContentValues();
        values.put(NAME_CONTACTS, contact.getName());
        values.put(TEL_CONTACTS, contact.getTel());
        db.update(TABLE_CONTACTS, values, KEY_ID_CONTACTS + "= ?",
                new String[]{String.valueOf(contact.getId())});
    }

    public void createAppointment(SQLiteDatabase db, Appointment appointment){
        ContentValues values = new ContentValues();
        values.put(TITLE_APPOINTMENTS, appointment.getTitle());
        values.put(DATE_APPOINTMENTS, appointment.getDate());
        values.put(TIME_APPOINTMENTS, appointment.getTime());
        values.put(PLACE_APPOINTMENTS, appointment.getPlace());
        values.put(MSG_APPOINTMENTS, appointment.getMsg());
        db.insert(TABLE_APPOINTMENTS,null,values);
    }

    public List<Appointment> printAppointments(SQLiteDatabase db) {
        List<Appointment> appointmentList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_APPOINTMENTS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Appointment appointment = new Appointment();
                appointment.setId(cursor.getInt(0));
                appointment.setTitle(cursor.getString(1));
                appointment.setDate(cursor.getString(2));
                appointment.setTime(cursor.getString(3));
                appointment.setPlace(cursor.getString(4));
                appointment.setMsg(cursor.getString(5));
                appointmentList.add(appointment);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return appointmentList;
    }

    // Method that returns av list containing this very day´s appointments
    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Appointment> findAppointments(SQLiteDatabase db) {
        List<Appointment> todaysAppointments = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_APPOINTMENTS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Appointment appointment = new Appointment();
                appointment.setTitle(cursor.getString(1));
                appointment.setDate(cursor.getString(2));
                if (Objects.equals(appointment.getDate(), LocalDate.now().toString())) {  // Today?
                    appointment.setId(cursor.getInt(0));
                    appointment.setTime(cursor.getString(3));
                    appointment.setPlace(cursor.getString(4));
                    appointment.setMsg(cursor.getString(5));
                    todaysAppointments.add(appointment);
                }
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return todaysAppointments;
    }

    public void deleteContact(SQLiteDatabase db, String title) {
        db.delete(TABLE_APPOINTMENTS, TITLE_APPOINTMENTS + " =? ",
                new String[]{title});
    }
}
