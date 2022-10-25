package com.example.oblig2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    static String TABLE_CONTACTS = "Contacts";
    static String KEY_ID = "_ID";
    static String KEY_NAME = "Name";
    static String KEY_TEL = "Tel";
    static int DATABASE_VERSION = 3;
    static String DATABASE_NAME = "DB_Contacts";

    public DBHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT," + KEY_TEL +
                " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    public void addContact(SQLiteDatabase db, Contact contact){
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_TEL, contact.getTel());
        db.insert(TABLE_CONTACTS,null,values);
    }

    public List<Contact> printContacts(SQLiteDatabase db) {
        List<Contact> contactList = new ArrayList<Contact>();
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

    public void deleteContact(SQLiteDatabase db, int inn_id) {
        db.delete(TABLE_CONTACTS, KEY_ID + " =? ",
            new String[]{Long.toString(inn_id)});
    }

    public int updateContact(SQLiteDatabase db, Contact contact) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_TEL, contact.getTel());
        int modified = db.update(TABLE_CONTACTS, values, KEY_ID + "= ?",
                new String[]{String.valueOf(contact.getId())});
        return modified;
    }
}
