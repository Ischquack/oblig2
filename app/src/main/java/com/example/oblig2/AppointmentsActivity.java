package com.example.oblig2;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class AppointmentsActivity extends AppCompatActivity {

    EditText inTitle;
    EditText inDate;
    EditText inTime;
    EditText inPlace;
    EditText inMsg;
    TableLayout printAppointments;
    DBHandler dbHelper;
    SQLiteDatabase db;

    public void createAppointment(View v) {
        if(validateInput()){
            Appointment appointment = new Appointment(inTitle.getText().toString(),
                    inDate.getText().toString(), inTime.getText().toString(),
                    inPlace.getText().toString(), inMsg.getText().toString());
            resetInputFields();
            dbHelper.createAppointment(db, appointment);
            printAppointment(v);
        }
        else resetInputFields();
    }

    public void printAppointment(View v) {
        List<Appointment> appointments = dbHelper.printAppointments(db);
        TableLayout tlPrintAppointments = (TableLayout) findViewById(R.id.tlPrintAppointments);
        tlPrintAppointments.removeAllViews();
        TableRow tbRowHeader = new TableRow(this);
        TextView tvTitle = new TextView(this);
        tvTitle.setPadding(0,0,40,25);
        tvTitle.setText("Title");
        tbRowHeader.addView(tvTitle);
        TextView tvDate = new TextView(this);
        tvDate.setPadding(0,0,40,25);
        tvDate.setText("Date");
        tbRowHeader.addView(tvDate);
        TextView tvTime = new TextView(this);
        tvTime.setPadding(0,0,40,25);
        tvTime.setText("Time");
        tbRowHeader.addView(tvTime);
        TextView tvPlace = new TextView(this);
        tvPlace.setPadding(0,0,40,25);
        tvPlace.setText("Place");
        tbRowHeader.addView(tvPlace);
        tlPrintAppointments.addView(tbRowHeader);

        for (Appointment appointment : appointments) {
            TableRow tbRow  =  new TableRow(this);
            TextView tv1 = new TextView(this);
            tv1.setPadding(0,0,40,25);
            tv1.setText(appointment.getTitle());
            tbRow.addView(tv1);
            TextView tv2 = new TextView(this);
            tv2.setPadding(0,0,40,25);
            tv2.setText(appointment.getDate());
            tbRow.addView(tv2);
            TextView tv3 = new TextView(this);
            tv3.setPadding(0,0,40,25);
            tv3.setText(appointment.getTime());
            tbRow.addView(tv3);
            TextView tv4 = new TextView(this);
            tv4.setPadding(0,0,40,25);
            tv4.setText(appointment.getPlace());
            tbRow.addView(tv4);

            tlPrintAppointments.addView(tbRow);
        }
    }

    public void deleteAppointment(View v) {
        String title = inTitle.getText().toString();
        resetInputFields();
        dbHelper.deleteContact(db, title);
        printAppointment(v);
    }

    public void resetInputFields(){
        inTitle.setText("");
        inDate.setText("");
        inTime.setText("");
        inPlace.setText("");
        inMsg.setText("");
    }

    public boolean validateInput(){
        String regexTitle = "[A-Za-zÆØÅæøå \\-]{2,25}";
        String titleTest = inTitle.getText().toString();
        String regexDate = "([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))";
        String dateTest = inDate.getText().toString();
        String regexTime = "[0-2][0-9]:[0-5][0-9]";
        String timeTest = inTime.getText().toString();
        boolean ok = titleTest.matches(regexTitle) && dateTest.matches(regexDate) &&
                timeTest.matches(regexTime);
        if (!ok){
            if(!titleTest.matches(regexTitle)){
                Toast.makeText(this,"Title",Toast.LENGTH_LONG).show();
            }
            if(!dateTest.matches(regexDate)){
                Toast.makeText(this,"Date",Toast.LENGTH_LONG).show();
            }
            if(!timeTest.matches(regexTime)){
                Toast.makeText(this,"Time",Toast.LENGTH_LONG).show();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        inTitle = (EditText) findViewById(R.id.etInTitle);
        inDate = (EditText) findViewById(R.id.etInDate);
        inTime = (EditText) findViewById(R.id.etInTime);
        inPlace = (EditText) findViewById(R.id.etInPlace);
        inMsg = (EditText) findViewById(R.id.etInMsg);
        printAppointments = (TableLayout) findViewById(R.id.tlPrintAppointments);
        dbHelper = new DBHandler(this);
        db = dbHelper.getWritableDatabase();

        Button btnCreateAppointment = (Button) findViewById(R.id.btnCreateAppointment);
        btnCreateAppointment.setOnClickListener(this::createAppointment);

        Button btnPrintAppointment = (Button) findViewById(R.id.btnPrintAppointments);
        btnPrintAppointment.setOnClickListener(this::printAppointment);

        Button btnDeleteAppointment = (Button) findViewById(R.id.btnDeleteAppointment);
        btnDeleteAppointment.setOnClickListener(this::deleteAppointment);
    }


}