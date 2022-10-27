package com.example.oblig2;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/** This ContactProvider class gives other applications the opportunity to access data from the
    Contact table in the database.
*/

public class ContactProvider extends ContentProvider {
    DBHandler dbHelper;
    SQLiteDatabase db;

    private final static String TABLE_CONTACTS = DBHandler.TABLE_CONTACTS;
    public static final String KEY_ID_CONTACTS = DBHandler.KEY_ID_CONTACTS;
    public final static String PROVIDER = "com.example.oblig2";     // Provider path
    // Variables  used for comparison:
    private static final int CONTACTS = 1;
    private static final int MCONTACTS = 2;

    // CONTENT_URI was used when we tested this ContentProvider
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER + "/Contacts");
    private static final UriMatcher uriMatcher;
    // Adding uri's to uriMatcher for later comparison:
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER, "Contacts", MCONTACTS);
        uriMatcher.addURI(PROVIDER, "Contacts/#", CONTACTS);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHandler(getContext());
        db = dbHelper.getWritableDatabase();
        return true;
    }

    // Method for making queries to database
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s,
                        @Nullable String[] strings1, @Nullable String s1) {
        Cursor cur = db.query(TABLE_CONTACTS, strings,
                KEY_ID_CONTACTS+"="+uri.getPathSegments().get(1),
                null, null, null, null);
        Log.d("cursor", cur.toString());
        return cur;
    }

    // Method that checks uris and returns access to either table or item in table
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case MCONTACTS:
                return "vnd.android.cursor.dir/vnd.example.Contacts";
            case CONTACTS:
                return "vnd.android.cursor.item/vnd.example.Contacts";
            default:
                throw new
                        IllegalArgumentException("Invalid URI" + uri);
        }
    }

    // Method for adding a contact to database
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        db.insert(TABLE_CONTACTS, null, contentValues);
        Cursor cur = db.query(TABLE_CONTACTS, null, null, null,
                null, null, null);
        cur.moveToLast();
        long myId = cur.getLong(0);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, myId);
    }

    // Method for deleting a contact from database
    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        if (uriMatcher.match(uri) == CONTACTS) {
            db.delete(TABLE_CONTACTS, KEY_ID_CONTACTS + "=" +
                    uri.getPathSegments().get(1), strings);
            getContext().getContentResolver().notifyChange(uri, null);
            return 1;
        }
        if (uriMatcher.match(uri) == MCONTACTS) {
            db.delete(TABLE_CONTACTS, null, null);
            getContext().getContentResolver().notifyChange(uri, null);
            return 2;
        }
        return 0;
    }

    // Method for updating a contact in the database
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s,
                      @Nullable String[] strings) {
        if (uriMatcher.match(uri) == CONTACTS) {
            db.update(TABLE_CONTACTS, contentValues, KEY_ID_CONTACTS + "=" +
                    uri.getPathSegments().get(1), null);
            getContext().getContentResolver().notifyChange(uri, null);
            return 1;
        }
        if (uriMatcher.match(uri) == MCONTACTS) {
            db.update(TABLE_CONTACTS, null, null, null);
            getContext().getContentResolver().notifyChange(uri, null);
            return 2;
        }
        return 0;
    }
}
