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

public class DBHandler extends SQLiteOpenHelper {
    static String TABLE_APPOINTMENTS = "Appointments";
    static String KEY_ID_APPOINTMENTS = "_ID";
    static String KEY_TITLE_APPOINTMENTS = "Title";
    static String KEY_DATE_APPOINTMENTS = "Date";
    static String KEY_TIME_APPOINTMENTS = "Time";
    static String KEY_PLACE_APPOINTMENTS = "Place";
    static String KEY_MSG_APPOINTMENTS = "Message";
    static String TABLE_CONTACTS = "Contacts";
    static String KEY_ID_CONTACTS = "_ID";
    static String KEY_NAME_CONTACTS = "Name";
    static String KEY_TEL_CONTACTS = "Tel";
    static int DATABASE_VERSION = 3;
    static String DATABASE_NAME = "DB_AppManager";

    public DBHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_CONTACTS = "CREATE TABLE " + TABLE_CONTACTS + "(" + KEY_ID_CONTACTS
                + " INTEGER PRIMARY KEY," + KEY_NAME_CONTACTS + " TEXT," + KEY_TEL_CONTACTS +
                " TEXT)";
        db.execSQL(CREATE_TABLE_CONTACTS);
        String CREATE_TABLE_APPOINTMENTS = "CREATE TABLE " + TABLE_APPOINTMENTS + "(" +
                KEY_ID_APPOINTMENTS + " INTEGER PRIMARY KEY," + KEY_TITLE_APPOINTMENTS + " TEXT,"
                + KEY_DATE_APPOINTMENTS + " DATE," + KEY_TIME_APPOINTMENTS + " TIME," +
                KEY_PLACE_APPOINTMENTS + " TEXT," + KEY_MSG_APPOINTMENTS + " TEXT)";
        db.execSQL(CREATE_TABLE_APPOINTMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENTS);
        onCreate(db);
    }



    public void addContact(SQLiteDatabase db, Contact contact){
        ContentValues values = new ContentValues();
        values.put(KEY_NAME_CONTACTS, contact.getName());
        values.put(KEY_TEL_CONTACTS, contact.getTel());
        db.insert(TABLE_CONTACTS,null,values);
    }

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
            while (cursor.moveToNext());
            cursor.close();
        }
        return contactList;
    }

    public void deleteContact(SQLiteDatabase db, int inId) {
        db.delete(TABLE_CONTACTS, KEY_ID_CONTACTS + " =? ",
            new String[]{Long.toString(inId)});
    }

    public int updateContact(SQLiteDatabase db, Contact contact) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME_CONTACTS, contact.getName());
        values.put(KEY_TEL_CONTACTS, contact.getTel());
        int modified = db.update(TABLE_CONTACTS, values, KEY_ID_CONTACTS + "= ?",
                new String[]{String.valueOf(contact.getId())});
        return modified;
    }

    public void createAppointment(SQLiteDatabase db, Appointment appointment){
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE_APPOINTMENTS, appointment.getTitle());
        values.put(KEY_DATE_APPOINTMENTS, appointment.getDate());
        values.put(KEY_TIME_APPOINTMENTS, appointment.getTime());
        values.put(KEY_PLACE_APPOINTMENTS, appointment.getPlace());
        values.put(KEY_MSG_APPOINTMENTS, appointment.getMsg());
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Appointment> findAppointments(SQLiteDatabase db) {
        Log.d("test", "Inside findAppointments");
        List<Appointment> todaysAppointments = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_APPOINTMENTS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Appointment appointment = new Appointment();
                appointment.setTitle(cursor.getString(1));
                if (appointment.getDate().equals(LocalDate.now().toString())) {
                    appointment.setId(cursor.getInt(0));
                    appointment.setDate(cursor.getString(2));
                    appointment.setTime(cursor.getString(3));
                    appointment.setPlace(cursor.getString(4));
                    appointment.setMsg(cursor.getString(5));
                    todaysAppointments.add(appointment);
                }
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        Log.d("test", "Returning todays appointments");
        return todaysAppointments;
    }

    public void deleteContact(SQLiteDatabase db, String title) {
        db.delete(TABLE_APPOINTMENTS, KEY_TITLE_APPOINTMENTS + " =? ",
                new String[]{title});
    }
}
