package com.example.oblig2;

import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import com.example.oblig2.databinding.ActivityFriendsBinding;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class FriendsActivity extends AppCompatActivity {

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

    public void deleteContact(View v) {
        int id = (Integer.parseInt(inId.getText().toString()));
        dbHelper.deleteContact(db,id);
    }

    public void updateContact(View v) {
        Contact contact = new Contact();
        contact.setName(inName.getText().toString());
        contact.setTel(inTel.getText().toString());
        contact.setId(Integer.parseInt(inId.getText().toString()));
        dbHelper.updateContact(db, contact);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        final Resources res;

        inName = (EditText) findViewById(R.id.etInName);
        inTel = (EditText) findViewById(R.id.etInTel);
        inId = (EditText) findViewById(R.id.etInId);
        printContacts = (TextView) findViewById(R.id.tvPrintContacts);
        dbHelper = new DBHandler(this);
        db = dbHelper.getWritableDatabase();

        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addContact(v);
            }
        });

        Button btnPrintContacts = (Button) findViewById(R.id.btnPrintContacts);
        btnPrintContacts.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                printContacts(v);
            }
        });

        Button btnDeleteContact = (Button) findViewById(R.id.btnDel);
        btnDeleteContact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteContact(v);
            }
        });

        Button btnUpdateContact = (Button) findViewById(R.id.btnDelApp);
        btnUpdateContact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateContact(v);
            }
        });
    }




}