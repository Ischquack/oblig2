package com.example.oblig2;

import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.oblig2.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText inName;
    EditText inTel;
    EditText inId;
    TextView printContacts;
    DBHandler dbHelper;
    SQLiteDatabase db;

    public void addContact(View v) {
        Contact contact = new Contact(inName.getText().toString(),
                inTel.getText().toString());
        dbHelper.addContact(db, contact);
    }

    public void printContacts(View v) {
        String text = "";
        List<Contact> contacts = dbHelper.printContacts(db);
        for (Contact contact : contacts) {
            text = text + "Id: " + contact.getId() + ",Name: " +
                    contact.getName() + " ,Tel: " +
                    contact.getTel() + "\n";
        }
        printContacts.setText(text);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Resources res;

        inName = (EditText) findViewById(R.id.etInName);
        inTel = (EditText) findViewById(R.id.etInTel);
        inId = (EditText) findViewById(R.id.etInId);
        printContacts = (TextView) findViewById(R.id.tvPrintContacts);
        dbHelper = new DBHandler(this);
        db = dbHelper.getWritableDatabase();

        
    }




}