package com.example.oblig2;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
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

        inName = (EditText) findViewById(R.id.etInName);
        inTel = (EditText) findViewById(R.id.etInTel);
        inId = (EditText) findViewById(R.id.etInId);
        printContacts = (TextView) findViewById(R.id.tvPrintContacts);
        dbHelper = new DBHandler(this);
        db = dbHelper.getWritableDatabase();

        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this::addContact);

        Button btnPrintContacts = (Button) findViewById(R.id.btnPrintContacts);
        btnPrintContacts.setOnClickListener(this::printContacts);

        Button btnDeleteContact = (Button) findViewById(R.id.btnDel);
        btnDeleteContact.setOnClickListener(this::deleteContact);

        Button btnUpdateContact = (Button) findViewById(R.id.btnUpdateContact);
        btnUpdateContact.setOnClickListener(this::updateContact);
    }

    /*@Override
    protected void onDestroy() {
        this.deleteDatabase("DB_AppointmentManager");
        dbHelper.close();
        super.onDestroy();
    }*/


}