package com.example.oblig2;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class AppointmentsActivity extends AppCompatActivity {

    EditText inTitle;
    EditText inDate;
    EditText inTime;
    EditText inPlace;
    EditText inMsg;
    TextView printAppointments;
    DBHandler dbHelper;
    SQLiteDatabase db;

    public void createAppointment(View v) {
        Appointment appointment = new Appointment(inTitle.getText().toString(),
                inDate.getText().toString(), inTime.getText().toString(),
                inPlace.getText().toString(), inMsg.getText().toString());
        dbHelper.createAppointment(db, appointment);
    }

    public void printAppointment(View v) {
        String text = "";
        List<Appointment> appointments = dbHelper.printAppointments(db);
        for (Appointment appointment : appointments) {
            text = text + "Id: " + appointment.getId() + ",Title: " +
                    appointment.getTitle() + " ,Date: " +
                    appointment.getDate() + " ,Time: " + appointment.getTime() +
                    " ,Place: " + appointment.getPlace() + " ,Message: " + appointment.getMsg() +
                    "\n";
        }
        printAppointments.setText(text);
    }

    public void deleteAppointment(View v) {
        String title = inTitle.getText().toString();
        dbHelper.deleteContact(db, title);
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
        printAppointments = (TextView) findViewById(R.id.tvPrintAppointments);
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