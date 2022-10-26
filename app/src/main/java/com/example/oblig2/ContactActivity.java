package com.example.oblig2;

import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.List;

public class ContactActivity extends AppCompatActivity {

    EditText inName;
    EditText inTel;
    EditText inId;
    TableLayout printContacts;
    DBHandler dbHelper;
    SQLiteDatabase db;

    public void addContact(View v) {
        Contact contact = new Contact(inName.getText().toString(),
                inTel.getText().toString());
        dbHelper.addContact(db, contact);
    }

    public void printContacts(View v) {
        List<Contact> contacts = dbHelper.printContacts(db);
        TableLayout tlPrintContacts = (TableLayout) findViewById(R.id.tlPrintContacts);
        tlPrintContacts.removeAllViews();
        TableRow tbRowHeader = new TableRow(this);
        TextView tvId = new TextView(this);
        tvId.setPadding(0,0,40,25);
        tvId.setText("ID");
        tbRowHeader.addView(tvId);
        TextView tvName = new TextView(this);
        tvName.setPadding(0,0,40,25);
        tvName.setText("Name");
        tbRowHeader.addView(tvName);
        TextView tvTel = new TextView(this);
        tvTel.setPadding(0,0,40,25);
        tvTel.setText("Phone number");
        tbRowHeader.addView(tvTel);
        tlPrintContacts.addView(tbRowHeader);

        for (Contact contact : contacts) {
            TableRow tbRow  =  new TableRow(this);
            TextView tv0 = new TextView(this);
            tv0.setPadding(0,0,40,25);
            tv0.setText(Integer.toString(contact.getId()));
            tbRow.addView(tv0);
            TextView tv1 = new TextView(this);
            tv1.setPadding(0,0,40,25);
            tv1.setText(contact.getName());
            tbRow.addView(tv1);
            TextView tv2 = new TextView(this);
            tv2.setPadding(0,0,40,25);
            tv2.setText(contact.getTel());
            tbRow.addView(tv2);
            tlPrintContacts.addView(tbRow);
        }
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
        setContentView(R.layout.activity_contact);

        inName = (EditText) findViewById(R.id.etInName);
        inTel = (EditText) findViewById(R.id.etInTel);
        inId = (EditText) findViewById(R.id.etInId);
        printContacts = (TableLayout) findViewById(R.id.tlPrintContacts);
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
}