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

public class ContactProvider extends ContentProvider {
    DBHandler dbHelper;
    SQLiteDatabase db;
    private final static String TABLE_CONTACTS = DBHandler.TABLE_CONTACTS;
    public static final String KEY_ID_CONTACTS = DBHandler.KEY_ID_CONTACTS;
    public static final String NAME_CONTACTS = DBHandler.NAME_CONTACTS;
    private static final String TEL_CONTACTS = DBHandler.TEL_CONTACTS;
    private static final String DATABASE_NAME = DBHandler.DATABASE_NAME;
    private static final int DATABASE_VERSION = DBHandler.DATABASE_VERSION;
    public final static String PROVIDER = "com.example.oblig2";
    private static final int CONTACTS = 1;
    private static final int MCONTACTS = 2;

    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER + "/Contacts");
    private static final UriMatcher uriMatcher;
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

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Cursor cur = db.query(TABLE_CONTACTS, strings, KEY_ID_CONTACTS+"="+uri.getPathSegments().get(1), null, null, null, null);
        Log.d("cursor", cur.toString());
        return cur;
    }

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

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        db.insert(TABLE_CONTACTS, null, contentValues);
        Cursor cur = db.query(TABLE_CONTACTS, null, null, null, null, null, null);
        cur.moveToLast();
        long myId = cur.getLong(0);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, myId);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
