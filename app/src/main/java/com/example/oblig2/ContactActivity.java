package com.example.oblig2;

import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ContactActivity extends AppCompatActivity {

    EditText inName;
    EditText inTel;
    EditText inId;
    TableLayout printContacts;
    DBHandler dbHelper;
    SQLiteDatabase db;

    public void addContact(View v) {
        if(validateInput()){
            //Creates contact object that receives values from input fields in its constructor
            Contact contact = new Contact(inName.getText().toString(),
                    inTel.getText().toString());
            resetInputFields();     // All EditTexts gets cleared
            dbHelper.addContact(db, contact);   // Uses dbHelper to access the database
            printContacts(v);
        }
        else resetInputFields();
    }

    //Method that prints the whole Contacts database table to the view.
    public void printContacts(View v) {
        List<Contact> contacts = dbHelper.printContacts(db);    // Retrieves the Contact table
        // A TableLayout view is created inside a SrollView from activity_contact.xml.:
        TableLayout tlPrintContacts = (TableLayout) findViewById(R.id.tlPrintContacts);
        // Removes the printed table before printing the updated table
        tlPrintContacts.removeAllViews();
        // This code section handles the table header:
        TableRow tbRowHeader = new TableRow(this);  // New TableRow inside TableLayout
        TextView tvId = new TextView(this);         // New TextView (column) inside TableRow
        tvId.setPadding(25,0,25,25);  // Handles spacing between elements
        tvId.setText("ID");
        tbRowHeader.addView(tvId);                          // Adds column til row
        TextView tvName = new TextView(this);
        tvName.setPadding(25,0,25,25);
        tvName.setText("Name");
        tbRowHeader.addView(tvName);
        TextView tvTel = new TextView(this);
        tvTel.setPadding(25,0,25,25);
        tvTel.setText("Phone number");
        tbRowHeader.addView(tvTel);
        tlPrintContacts.addView(tbRowHeader);               // Adds TableRow to the TableLayout

        // This code section adds a new row for each contact and columns containing contact data
        for (Contact contact : contacts) {
            TableRow tbRow  =  new TableRow(this);
            TextView tv0 = new TextView(this);
            tv0.setPadding(25,0,25,20);
            tv0.setText(Integer.toString(contact.getId()));
            tbRow.addView(tv0);
            TextView tv1 = new TextView(this);
            tv1.setPadding(25,0,25,20);
            tv1.setText(contact.getName());
            tbRow.addView(tv1);
            TextView tv2 = new TextView(this);
            tv2.setPadding(25,0,25,20);
            tv2.setText(contact.getTel());
            tbRow.addView(tv2);
            tlPrintContacts.addView(tbRow);
        }
    }

    public void deleteContact(View v) {
        int id = (Integer.parseInt(inId.getText().toString()));
        resetInputFields();
        dbHelper.deleteContact(db,id);
        printContacts(v);
    }

    public void updateContact(View v) {
        if(validateInput()){
            Contact contact = new Contact();
            contact.setName(inName.getText().toString());
            contact.setTel(inTel.getText().toString());
            contact.setId(Integer.parseInt(inId.getText().toString()));
            resetInputFields();
            dbHelper.updateContact(db, contact);
            printContacts(v);
        }
        else resetInputFields();
    }

    // Method that clears all EditTexts. Gets called in addContact(), updateContact()
    // and deleteContact()
    public void resetInputFields(){
        inName.setText("");
        inTel.setText("");
        inId.setText("");
    }

    // Method that handles input validation when adding and updating contacts
    public boolean validateInput(){
        String regexNavn = "[A-Za-zÆØÅæøå \\-]{2,25}";      // 2-25 characters
        String nameTest = inName.getText().toString();
        String regexTel = "[0-9]{8,12}";                    // 8-12 digits
        String telTest = inTel.getText().toString();
        // ok variable tests if the inputs pass the regular expressions provided
        boolean ok = nameTest.matches(regexNavn) && telTest.matches(regexTel);

        if (!ok){
            if(!nameTest.matches(regexNavn)){
                Toast.makeText(this,"Invalid name given",Toast.LENGTH_LONG).show();
                return false;
            }
            if(!telTest.matches(regexTel)){
                Toast.makeText(this,"Invalid number",Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;                                // If ok variable returned true
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
        db = dbHelper.getWritableDatabase();    // db gets access to read and write in database

        // Add contact button
        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this::addContact);

        // Print contacts button
        Button btnPrintContacts = (Button) findViewById(R.id.btnPrintContacts);
        btnPrintContacts.setOnClickListener(this::printContacts);

        // Delete contact  button
        Button btnDeleteContact = (Button) findViewById(R.id.btnDel);
        btnDeleteContact.setOnClickListener(this::deleteContact);

        // Update contact button
        Button btnUpdateContact = (Button) findViewById(R.id.btnUpdateContact);
        btnUpdateContact.setOnClickListener(this::updateContact);
    }
}